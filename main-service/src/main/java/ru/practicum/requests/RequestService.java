package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.events.Event;
import ru.practicum.events.EventRepository;
import ru.practicum.events.State;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public RequestDto create(int userId, int eventId) {
        if (eventRepository.existsById(eventId) && userRepository.existsById(userId)) {
            Event event = eventRepository.findById(eventId).get();
            if (event.getState().equals(State.PUBLISHED)) {
                if (event.getInitiator().getId() != userId) {
                    if (event.getParticipantLimit() > event.getConfirmedRequests() ||
                            event.getParticipantLimit() == 0) {
                        if (!requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
                            Request request = new Request();
                            request.setCreated(LocalDateTime.now());
                            if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
                                request.setStatus(Status.PENDING);
                            } else {
                                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                                eventRepository.save(event);
                                request.setStatus(Status.CONFIRMED);
                            }
                            request.setRequester(userRepository.findById(userId).get());
                            request.setEvent(event);
                            requestRepository.save(request);
                            return RequestMapper.toRequestDto(request);
                        } else {
                            throw new ConflictException("You already have a requester in this event");
                        }
                    } else {
                        throw new ConflictException("Participant limit exceeded");
                    }
                } else {
                    throw new ConflictException("It is your event");
                }
            } else {
                throw new ConflictException("Must be published");
            }
        } else {
            throw new NotFoundException("Event or user not found");
        }
    }

    public List<RequestDto> findAllByRequesterId(int userId) {
        if (userRepository.existsById(userId)) {
            return requestRepository.findAllByRequesterId(userId).stream()
                    .map(RequestMapper::toRequestDto)
                    .toList();

        } else {
            throw new NotFoundException("User not found");
        }
    }

    public RequestDto cancel(int userId, int requestId) {
        if (requestRepository.existsById(requestId) && userRepository.existsById(userId)) {
            Request request = requestRepository.findById(requestId).get();
            request.setStatus(Status.CANCELED);
            requestRepository.save(request);
            return RequestMapper.toRequestDto(request);
        } else {
            throw new NotFoundException("Request or user not found");
        }
    }


}
