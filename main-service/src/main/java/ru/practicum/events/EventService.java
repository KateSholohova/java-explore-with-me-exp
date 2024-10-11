package ru.practicum.events;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.StatClient;
import ru.practicum.categories.Category;
import ru.practicum.categories.CategoryRepository;
import ru.practicum.events.location.Location;
import ru.practicum.events.location.LocationRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.requests.*;
import ru.practicum.users.User;
import ru.practicum.users.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final StatClient statClient;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventFullDto create(NewEventDto newEventDto, int userId) {
        if (LocalDateTime.parse(newEventDto.getEventDate(), formatter).isBefore(LocalDateTime.now()) ||
                LocalDateTime.parse(newEventDto.getEventDate(), formatter).equals(LocalDateTime.now())) {
            throw new ValidationException("Date of event cannot be in the past");
        }
        if (newEventDto.getPaid() == null) {
            newEventDto.setPaid(false);
        }
        if (newEventDto.getRequestModeration() == null) {
            newEventDto.setRequestModeration(true);
        }
        Event event = EventMapper.fromNewEventDroToEvent(newEventDto);
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        event.setCategory(category);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        log.info("user: " + user);
        event.setInitiator(userRepository.findById(userId).get());
        Location location = locationRepository.save(newEventDto.getLocation());
        event.setLocation(location);
        event.setCreatedOn(LocalDateTime.now());
        event.setPublishedOn(LocalDateTime.now());
        event.setConfirmedRequests(0);
        event.setState(State.PENDING);
        event.setViews(0);
        eventRepository.save(event);
        return EventMapper.toEventFullDto(event);
    }

    public List<EventFullDto> findAllByInitiatorId(int from, int size, int userId) {
        if (userRepository.existsById(userId)) {
            return eventRepository.findAllByInitiatorId(userId).stream()
                    .map(EventMapper::toEventFullDto)
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());

        } else {
            throw new NotFoundException("User not found");
        }
    }

    public EventFullDto findEventByInitiatorId(int userId, int eventId) {
        if (userRepository.existsById(userId)) {
            if (eventRepository.existsById(eventId)) {
                Event event = eventRepository.findById(eventId).get();
                if (event.getInitiator().getId() == userId) {
                    return EventMapper.toEventFullDto(event);
                } else {
                    throw new NotFoundException("Event not found");
                }

            } else {
                throw new NotFoundException("Event not found");
            }
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public EventFullDto updateByInitiator(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest) {
        if (userRepository.existsById(userId)) {
            if (eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
                if (eventRepository.findById(eventId).get().getState() == State.PENDING ||
                        eventRepository.findById(eventId).get().getState() == State.CANCELED) {
                    Event event = eventRepository.findById(eventId).get();
                    if (updateEventUserRequest.getStateAction() != null) {
                        if (updateEventUserRequest.getStateAction().equals(StateActionUser.CANCEL_REVIEW)) {
                            event.setState(State.CANCELED);
                        }
                        if (updateEventUserRequest.getStateAction().equals(StateActionUser.SEND_TO_REVIEW)) {
                            event.setState(State.PENDING);
                        }
                    }
                    return getEventFullDto(event, updateEventUserRequest.getParticipantLimit(),
                            updateEventUserRequest.getEventDate(), updateEventUserRequest.getLocation(),
                            updateEventUserRequest.getDescription(), updateEventUserRequest.getAnnotation(),
                            updateEventUserRequest.getTitle(), updateEventUserRequest.getCategory(),
                            updateEventUserRequest.getPaid(), updateEventUserRequest.getRequestModeration());

                }
                throw new ConflictException("Event must not be published");
            } else {
                throw new NotFoundException("Event not found");
            }
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public EventFullDto updateByAdmin(int eventId, UpdateEventAdminRequest updateEventAdminRequest) {

        if (eventRepository.existsById(eventId)) {
            Event event = eventRepository.findById(eventId).get();
            if (updateEventAdminRequest.getStateAction() != null) {
                if (updateEventAdminRequest.getStateAction().equals(StateActionAdmin.REJECT_EVENT) &&
                        !event.getState().equals(State.PUBLISHED)) {
                    event.setState(State.CANCELED);
                } else if (updateEventAdminRequest.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT) &&
                        event.getState().equals(State.PENDING)) {
                    event.setState(State.PUBLISHED);
                    eventRepository.save(event);
                } else {
                    throw new ConflictException("Event must be pending");
                }
            }

            return getEventFullDto(event, updateEventAdminRequest.getParticipantLimit(),
                    updateEventAdminRequest.getEventDate(), updateEventAdminRequest.getLocation(),
                    updateEventAdminRequest.getDescription(), updateEventAdminRequest.getAnnotation(),
                    updateEventAdminRequest.getTitle(), updateEventAdminRequest.getCategory(),
                    updateEventAdminRequest.getPaid(), updateEventAdminRequest.getRequestModeration());
        } else {
            throw new NotFoundException("Event not found");
        }
    }


    public List<EventFullDto> searchAdmin(List<Integer> users, List<String> states, List<Integer> categories,
                                          String rangeEnd, String rangeStart, int from, int size) {
        LocalDateTime startDateTime = rangeStart != null ? LocalDateTime.parse(rangeStart, formatter) : null;
        LocalDateTime endDateTime = rangeEnd != null ? LocalDateTime.parse(rangeEnd, formatter) : null;
        if (endDateTime != null && startDateTime != null) {
            if (startDateTime.isAfter(endDateTime)) {
                throw new ValidationException("Start time must be before end time");
            }
        }
        if (users != null && categories != null) {
            return eventRepository.findAllByInitiatorIdInAndCategoryIdIn(users, categories)
                    .stream()
                    .filter(e -> ((states != null) ? states.contains(e.getState().toString()) : true)
                            &&
                            ((startDateTime != null && endDateTime != null)
                                    ? e.getEventDate().isAfter(startDateTime) && e.getEventDate().isBefore(endDateTime)
                                    : e.getEventDate().isAfter(LocalDateTime.now()))
                    )
                    .skip(from)
                    .limit(size)
                    .map(EventMapper::toEventFullDto)
                    .toList();
        } else if (users == null && categories != null) {
            return eventRepository.findAllByCategoryIdIn(categories)
                    .stream()
                    .filter(e -> ((states != null) ? states.contains(e.getState().toString()) : true)
                            &&
                            ((startDateTime != null && endDateTime != null)
                                    ? e.getEventDate().isAfter(startDateTime) && e.getEventDate().isBefore(endDateTime)
                                    : e.getEventDate().isAfter(LocalDateTime.now()))
                    )
                    .skip(from)
                    .limit(size)
                    .map(EventMapper::toEventFullDto)
                    .toList();
        } else if (users != null && categories == null) {
            return eventRepository.findAllByInitiatorIdIn(users)
                    .stream()
                    .filter(e -> ((states != null) ? states.contains(e.getState().toString()) : true)
                            &&
                            ((startDateTime != null && endDateTime != null)
                                    ? e.getEventDate().isAfter(startDateTime) && e.getEventDate().isBefore(endDateTime)
                                    : e.getEventDate().isAfter(LocalDateTime.now()))
                    )
                    .skip(from)
                    .limit(size)
                    .map(EventMapper::toEventFullDto)
                    .toList();
        } else {
            return eventRepository.findAll()
                    .stream()
                    .filter(e -> ((states != null) ? states.contains(e.getState().toString()) : true)
                            &&
                            ((startDateTime != null && endDateTime != null)
                                    ? e.getEventDate().isAfter(startDateTime) && e.getEventDate().isBefore(endDateTime)
                                    : e.getEventDate().isAfter(LocalDateTime.now()))
                    )
                    .skip(from)
                    .limit(size)
                    .map(EventMapper::toEventFullDto)
                    .toList();
        }
    }

    public List<EventFullDto> searchPublic(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                           String rangeEnd, Boolean onlyAvailable, String sort, int from, int size, HttpServletRequest request) {
        LocalDateTime startDateTime = rangeStart != null ? LocalDateTime.parse(rangeStart, formatter) : null;
        LocalDateTime endDateTime = rangeEnd != null ? LocalDateTime.parse(rangeEnd, formatter) : null;
        if (endDateTime != null && startDateTime != null) {
            if (startDateTime.isAfter(endDateTime)) {
                throw new ValidationException("Start time must be before end time");
            }
        }
        if (categories != null) {
            List<Event> eventFullDtoList = eventRepository.findAllByCategoryIdIn(categories)
                    .stream()
                    .filter(e -> e.getState().equals(State.PUBLISHED)
                            &&
                            ((text != null) ? (e.getAnnotation().toLowerCase().contains(text.toLowerCase()) ||
                                    e.getDescription().toLowerCase().contains(text.toLowerCase())) : true)
                            &&
                            ((paid != null) ? e.getPaid().equals(paid) : true)
                            &&
                            ((onlyAvailable != null) ? (onlyAvailable ? e.getParticipantLimit() >= e.getConfirmedRequests() : true) : true)
                            &&
                            ((startDateTime != null && endDateTime != null)
                                    ? e.getEventDate().isAfter(startDateTime) && e.getEventDate().isBefore(endDateTime)
                                    : e.getEventDate().isAfter(LocalDateTime.now()))
                    )
                    .skip(from)
                    .limit(size)
                    .sorted((sort != null && sort.equals("EVENT_DATE")) ? Comparator.comparing(Event::getEventDate).reversed() :
                            (sort != null && sort.equals("VIEWS")) ? Comparator.comparing(Event::getViews).reversed() :
                                    Comparator.comparing(Event::getEventDate))
                    .toList();

            for (Event e : eventFullDtoList) {
                e.setViews(e.getViews() + 1);
                eventRepository.save(e);
            }

            return eventFullDtoList.stream()
                    .map(EventMapper::toEventFullDto)
                    .toList();
        } else {
            List<Event> eventFullDtoList = eventRepository.findAll()
                    .stream()
                    .filter(e -> e.getState().equals(State.PUBLISHED)
                            &&
                            ((text != null) ? (e.getAnnotation().toLowerCase().contains(text.toLowerCase()) ||
                                    e.getDescription().toLowerCase().contains(text.toLowerCase())) : true)
                            &&
                            ((paid != null) ? e.getPaid().equals(paid) : true)
                            &&
                            ((onlyAvailable != null) ? (onlyAvailable ? e.getParticipantLimit() >= e.getConfirmedRequests() : true) : true)
                            &&
                            ((startDateTime != null && endDateTime != null)
                                    ? e.getEventDate().isAfter(startDateTime) && e.getEventDate().isBefore(endDateTime)
                                    : e.getEventDate().isAfter(LocalDateTime.now()))
                    )
                    .skip(from)
                    .limit(size)
                    .sorted((sort != null && sort.equals("EVENT_DATE")) ? Comparator.comparing(Event::getEventDate).reversed() :
                            (sort != null && sort.equals("VIEWS")) ? Comparator.comparing(Event::getViews).reversed() :
                                    Comparator.comparing(Event::getEventDate))
                    .toList();

            for (Event e : eventFullDtoList) {
                e.setViews(e.getViews() + 1);
                eventRepository.save(e);
            }

            return eventFullDtoList.stream()
                    .map(EventMapper::toEventFullDto)
                    .toList();
        }
    }

    public EventFullDto findByIdPublic(int id, HttpServletRequest request) {
        if (eventRepository.existsById(id)) {
            Event event = eventRepository.findById(id).get();
            if (event.getState().equals(State.PUBLISHED)) {
                event.setViews(event.getViews() + 1);
                eventRepository.save(event);
                return EventMapper.toEventFullDto(event);
            } else {
                throw new NotFoundException("Event not found");
            }
        } else {
            throw new NotFoundException("Event not found");
        }

    }

    public List<RequestDto> findRequestsByInitiatorId(int userId, int eventId) {
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toRequestDto)
                .toList();
    }

    public EventRequestStatusUpdateResult updateRequestStatus(int userId, int eventId,
                                                              EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {

        if (eventRepository.existsById(eventId)) {
            Event event = eventRepository.findById(eventId).get();
            if (event.getParticipantLimit() > event.getConfirmedRequests()) {
                List<Request> requests = requestRepository.findAllByIdIn(eventRequestStatusUpdateRequest.getRequestIds());
                List<Request> rejectedRequests = new ArrayList<>();
                List<Request> acceptedRequests = new ArrayList<>();
                for (Request request : requests) {
                    if (request.getStatus().equals(Status.PENDING)) {
                        if (eventRequestStatusUpdateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
                            if (event.getParticipantLimit() > event.getConfirmedRequests()) {
                                request.setStatus(Status.CONFIRMED);
                                acceptedRequests.add(request);
                                requestRepository.save(request);
                                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                                eventRepository.save(event);
                            } else {
                                request.setStatus(Status.REJECTED);
                                rejectedRequests.add(request);
                                requestRepository.save(request);
                            }

                        } else {
                            request.setStatus(Status.REJECTED);
                            rejectedRequests.add(request);
                            requestRepository.save(request);
                        }

                    } else {
                        throw new ConflictException("Request status must be PENDING");
                    }
                }

                EventRequestStatusUpdateResult updateResult = new EventRequestStatusUpdateResult();
                updateResult.setConfirmedRequests(acceptedRequests.stream()
                        .map(RequestMapper::toRequestDto)
                        .toList());
                updateResult.setRejectedRequests(rejectedRequests.stream()
                        .map(RequestMapper::toRequestDto)
                        .toList());
                return updateResult;

            } else {
                throw new ConflictException("Participant limit exceeded");
            }
        } else {
            throw new NotFoundException("Event not found");
        }
    }

    private EventFullDto getEventFullDto(Event event, int participantLimit, String eventDate, Location location, String description, String annotation, String title, int category, Boolean paid, Boolean requestModeration) {
        if (participantLimit != 0) {
            event.setParticipantLimit(participantLimit);
        }
        if (eventDate != null) {
            event.setEventDate(LocalDateTime.parse(eventDate, formatter));
        }
        if (location != null) {
            locationRepository.save(location);
            event.setLocation(location);
        }
        if (description != null) {
            event.setDescription(description);
        }
        if (annotation != null) {
            event.setAnnotation(annotation);
        }
        if (title != null) {
            event.setTitle(title);
        }
        if (category != 0) {
            event.setCategory(categoryRepository.findById(category).get());
        }
        if (paid != null) {
            event.setPaid(paid);
        }
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
        eventRepository.save(event);
        return EventMapper.toEventFullDto(event);
    }
}
