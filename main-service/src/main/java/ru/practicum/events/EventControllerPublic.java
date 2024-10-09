package ru.practicum.events;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventControllerPublic {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> searchPublic(@RequestParam(required = false) String text,
                                           @RequestParam(required = false) List<Integer> categories,
                                           @RequestParam(required = false) boolean paid,
                                           @RequestParam(required = false) String rangeStart,
                                           @RequestParam(required = false) String rangeEnd,
                                           @RequestParam(required = false) boolean onlyAvailable,
                                           @RequestParam(required = false) String sort,
                                           @RequestParam(defaultValue = "0")
                                           @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "10")
                                           @Positive Integer size) {
        return eventService.searchPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

    }

    @GetMapping("/{id}")
    public EventFullDto findByIdPublic(@PathVariable Integer id) {
        return eventService.findByIdPublic(id);
    }
}
