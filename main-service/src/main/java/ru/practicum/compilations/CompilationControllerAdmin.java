package ru.practicum.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationControllerAdmin {

    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto create(@RequestBody @Validated NewCompilationDto newCompilationDto) {
        return compilationService.create(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void delete(@PathVariable int compId) {
        compilationService.delete(compId);
    }

    @PostMapping("/{compId}")
    public CompilationDto update(@PathVariable int compId, @RequestBody @Validated NewCompilationDto newCompilationDto) {
        return compilationService.update(compId, newCompilationDto);
    }


}
