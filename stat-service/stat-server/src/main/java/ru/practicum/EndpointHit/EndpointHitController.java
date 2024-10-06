package ru.practicum.EndpointHit;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStats.ViewStats;
import ru.practicum.ViewStats.ViewStatsMapper;
import ru.practicum.ViewStatsDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EndpointHitController {
    private final EndpointHitService endpointHitService;
    private final EndpointHitMapper endpointHitMapper;
    private final ViewStatsMapper viewStatsMapper;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("hit")
    public void save(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        endpointHitService.save(endpointHitDto);
    }

    @GetMapping("stats")
    public List<ViewStatsDto> findByParams(@RequestParam String start,
                                           @RequestParam String end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(required = false) boolean unique) {
        List<ViewStats> viewStats = endpointHitService.findByParams(start, end, uris, unique);
        return viewStatsMapper.listViewStatsToListViewStatsDto(viewStats);
    }
}
