package ru.practicum.compilations;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationControllerPublic {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(required = false) boolean pinned,
                                       @RequestParam(defaultValue = "0")
                                       @PositiveOrZero Integer from,
                                       @RequestParam(defaultValue = "10")
                                       @Positive Integer size) {
        return compilationService.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto findById(@PathVariable int compId) {
        return compilationService.findById(compId);
    }
}
