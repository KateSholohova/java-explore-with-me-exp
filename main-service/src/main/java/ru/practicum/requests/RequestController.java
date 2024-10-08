package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public RequestDto create(@PathVariable int userId, @RequestParam int eventId) {
        return requestService.create(userId, eventId);
    }

    @GetMapping
    public List<RequestDto> get(@PathVariable int userId) {
        return requestService.findAllByRequesterId(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancel(@PathVariable int userId, @PathVariable int requestId) {
        return requestService.cancel(userId, requestId);
    }
}
