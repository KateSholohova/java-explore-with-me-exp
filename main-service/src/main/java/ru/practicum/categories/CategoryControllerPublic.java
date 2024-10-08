package ru.practicum.categories;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryControllerPublic {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> findAll(@RequestParam(defaultValue = "0")
                                     @PositiveOrZero Integer from,
                                     @RequestParam(defaultValue = "10")
                                     @Positive Integer size) {
        return categoryService.findAll(from, size);
    }

    @GetMapping("/{categoryId}")
    public CategoryDto findById(@PathVariable("categoryId") int categoryId) {
        return categoryService.findById(categoryId);
    }
}
