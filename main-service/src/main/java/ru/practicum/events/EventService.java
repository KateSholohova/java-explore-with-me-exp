package ru.practicum.events;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.categories.Category;
import ru.practicum.categories.CategoryRepository;
import ru.practicum.events.location.Location;
import ru.practicum.events.location.LocationRepository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.requests.Status;
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

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventFullDto create(NewEventDto newEventDto, int userId) {
        Event event = EventMapper.fromNewEventDroToEvent(newEventDto);
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        event.setCategory(category);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        event.setInitiator(user);
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
            List<EventFullDto> eventFullDtoList = eventRepository.findAllByInitiatorId(userId).stream()
                    .map(EventMapper::toEventFullDto)
                    .collect(Collectors.toList());
            return eventFullDtoList.subList(from, from + size);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public EventFullDto findEventByInitiatorId(int userId, int eventId) {
        if (userRepository.existsById(userId)) {
//            List<EventFullDto> eventFullDtoList= eventRepository.findAllByInitiatorId(userId).stream()
//                    .map(EventMapper::toEventFullDto)
//                    .toList();
//            return eventFullDtoList.stream()
//                    .filter(e -> e.getId() == eventId)
//                    .findFirst()
//                    .orElseThrow(() -> new NotFoundException("Event not found"));
            if (eventRepository.existsByIdAndInitiatorId(userId, eventId)) {
                return EventMapper.toEventFullDto(eventRepository.findEventByIdAndInitiatorId(userId, eventId));
            } else {
                throw new NotFoundException("Event not found");
            }
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public EventFullDto updateByInitiator(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest) {
        if (userRepository.existsById(userId)) {
            if (eventRepository.existsByIdAndInitiatorId(userId, eventId)) {
                if (eventRepository.findEventByIdAndInitiatorId(userId, eventId).getState() == State.PENDING ||
                        eventRepository.findEventByIdAndInitiatorId(userId, eventId).getState() == State.CANCELED) {
                    Event event = eventRepository.findById(eventId).get();
                    if (updateEventUserRequest.getStateAction().equals(StateActionUser.CANCEL_REVIEW)) {
                        event.setState(State.CANCELED);
                    }
                    if (updateEventUserRequest.getStateAction().equals(StateActionUser.SEND_TO_REVIEW)) {
                        event.setState(State.PENDING);
                    }
                    return checkParameters(event, updateEventUserRequest.getParticipantLimit(),
                            updateEventUserRequest.getEventDate(), updateEventUserRequest.getLocation(),
                            updateEventUserRequest.getDescription(), updateEventUserRequest.getAnnotation(),
                            updateEventUserRequest.getTitle(), updateEventUserRequest.getCategory(),
                            updateEventUserRequest.getPaid(), updateEventUserRequest.getRequestModeration());
                }
                throw new ValidationException("Event must not be published");
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
            if (updateEventAdminRequest.getStateAction().equals(StateActionAdmin.REJECT_EVENT) &&
                    !event.getState().equals(State.PUBLISHED)) {
                event.setState(State.CANCELED);
            } else if (updateEventAdminRequest.getStateAction() == StateActionAdmin.PUBLISH_EVENT &&
                    event.getState() == State.PENDING) {
                event.setState(State.PUBLISHED);
            } else {
                throw new ValidationException("Event must be pending");
            }

            return checkParameters(event, updateEventAdminRequest.getParticipantLimit(),
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
        List<Event> events = eventRepository.findAllByInitiatorIdInAndCategoryIdIn(users, categories).stream()
                .filter(e -> e.getEventDate().isBefore(LocalDateTime.parse(rangeStart, formatter)) &&
                        e.getEventDate().isAfter(LocalDateTime.parse(rangeEnd, formatter)) && states.contains(e.getState().toString()))
                .toList();
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList())
                .subList(from, from + size);
    }

    public List<EventFullDto> searchPublic(String text, List<Integer> categories, boolean paid, String rangeStart,
                                           String rangeEnd, boolean onlyAvailable, String sort, int from, int size) {
        LocalDateTime startDateTime = rangeStart != null ? LocalDateTime.parse(rangeStart, formatter) : null;
        LocalDateTime endDateTime = rangeEnd != null ? LocalDateTime.parse(rangeEnd, formatter) : null;

        List<Event> events = eventRepository.findAllByCategoryIdIn(categories)
                .stream()
                .filter(e -> e.getState().equals(State.PUBLISHED) &&
                        (e.getAnnotation().toLowerCase().contains(text.toLowerCase()) ||
                                e.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                        e.getPaid().equals(paid) &&
                        (onlyAvailable ? e.getParticipantLimit() >= e.getConfirmedRequests() : true) &&
                        ((rangeStart != null && rangeEnd != null)
                                ? e.getEventDate().isBefore(startDateTime) && e.getEventDate().isAfter(endDateTime)
                                : e.getEventDate().isAfter(LocalDateTime.now()))
                )
                .sorted(sort.equals("EVENT_DATE") ? Comparator.comparing(Event::getEventDate).reversed() : Comparator.comparing(Event::getViews).reversed())
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());

        return events.stream()
                .map(EventMapper::toEventFullDto)
                .toList();
    }

    public EventFullDto findByIdPublic(int id) {
        if (eventRepository.existsById(id)) {
            Event event = eventRepository.findById(id).get();
            if (event.getState().equals(State.PUBLISHED)) {
                return EventMapper.toEventFullDto(event);
            } else {
                throw new ValidationException("Event must be published");
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
            if (event.getParticipantLimit() < event.getConfirmedRequests()) {
                List<Request> requests = requestRepository.findAllByIdIn(eventRequestStatusUpdateRequest.getRequestIds());
                List<Request> rejectedRequests = new ArrayList<>();
                List<Request> acceptedRequests = new ArrayList<>();
                for (Request request : requests) {
                    if (request.getStatus().equals(Status.PENDING)) {
                        if (eventRequestStatusUpdateRequest.getStatus().equals(ru.practicum.events.Status.CONFIRMED)) {
                            if (event.getParticipantLimit() < event.getConfirmedRequests()) {
                                request.setStatus(Status.CONFIRMED);
                                acceptedRequests.add(request);
                                requestRepository.save(request);
                                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                            } else {
                                request.setStatus(Status.CANCELED);
                                rejectedRequests.add(request);
                                requestRepository.save(request);
                            }
                        } else {
                            request.setStatus(Status.CANCELED);
                            acceptedRequests.add(request);
                            requestRepository.save(request);
                        }
                    } else {
                        throw new RuntimeException("Request status must be PENDING");
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
                throw new ValidationException("Participant limit exceeded");
            }
        } else {
            throw new NotFoundException("Event not found");
        }
    }

    private EventFullDto checkParameters(Event event, int participantLimit, LocalDateTime eventDate, Location location,
                                         String description, String annotation, String title, int category,
                                         Boolean paid, Boolean requestModeration) {
        if (participantLimit != 0) {
            event.setParticipantLimit(participantLimit);
        }
        if (eventDate != null) {
            event.setEventDate(eventDate);
        }
        if (location != null) {
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
