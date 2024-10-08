package ru.practicum.events;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class EventControllerAdmin {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> searchAdmin(@RequestParam List<Integer> users,
                                     @RequestParam List<String> states,
                                     @RequestParam List<Integer> categories,
                                     @RequestParam String rangeStart,
                                     @RequestParam String rangeEnd,
                                     @RequestParam(defaultValue = "0")
                                         @PositiveOrZero Integer from,
                                     @RequestParam(defaultValue = "10")
                                         @Positive Integer size
                                     ) {
        return eventService.searchAdmin(users, states, categories, rangeEnd, rangeStart, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateByAdmin(@PathVariable("eventId") int eventId,
                                      @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest){
        return eventService.updateByAdmin(eventId, updateEventAdminRequest);
    }


}
