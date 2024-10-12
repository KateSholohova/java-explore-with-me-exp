package ru.practicum.events;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatClient;
import ru.practicum.config.AppConfig;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventControllerPublic {

    private final EventService eventService;
    private final StatClient statClient;
    private final AppConfig appConfig;

    @GetMapping
    public List<EventFullDto> searchPublic(@RequestParam(required = false) String text,
                                           @RequestParam(required = false) List<Integer> categories,
                                           @RequestParam(required = false) Boolean paid,
                                           @RequestParam(required = false) String rangeStart,
                                           @RequestParam(required = false) String rangeEnd,
                                           @RequestParam(required = false) Boolean onlyAvailable,
                                           @RequestParam(required = false) String sort,
                                           @RequestParam(defaultValue = "0")
                                           @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "10")
                                           @Positive Integer size,
                                           HttpServletRequest request) {
        statClient.saveHit(appConfig.getAppName(), request);
        return eventService.searchPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);

    }

    @GetMapping("/{id}")
    public EventFullDto findByIdPublic(@PathVariable Integer id,
                                       HttpServletRequest request) {
        statClient.saveHit(appConfig.getAppName(), request);
        return eventService.findByIdPublic(id, request);
    }
}
