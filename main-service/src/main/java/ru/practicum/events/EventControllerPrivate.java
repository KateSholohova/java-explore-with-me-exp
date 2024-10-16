package ru.practicum.events;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.RequestDto;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class EventControllerPrivate {

    private final EventService eventService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EventFullDto create(@RequestBody @Valid NewEventDto newEventDto,
                               @PathVariable("userId") int userId) {
        return eventService.create(newEventDto, userId);
    }

    @GetMapping
    public List<EventFullDto> findAllByInitiatorId(@RequestParam(defaultValue = "0")
                                                   @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10")
                                                   @Positive Integer size,
                                                   @PathVariable("userId") int userId) {
        return eventService.findAllByInitiatorId(from, size, userId);
    }

    @GetMapping("/{eventId}")
    public EventFullDto findEventByInitiatorId(@PathVariable("userId") int userId,
                                               @PathVariable("eventId") int eventId) {
        return eventService.findEventByInitiatorId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateByInitiator(@PathVariable("userId") int userId,
                                          @PathVariable("eventId") int eventId,
                                          @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return eventService.updateByInitiator(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> findRequestsByInitiatorId(@PathVariable("userId") int userId,
                                                      @PathVariable("eventId") int eventId) {
        return eventService.findRequestsByInitiatorId(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestStatus(@PathVariable("userId") int userId,
                                                              @PathVariable("eventId") int eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest updateEventUserRequest) {
        return eventService.updateRequestStatus(userId, eventId, updateEventUserRequest);
    }


}
