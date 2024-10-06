package ru.practicum.categories;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Validated
public class CategoryControllerAdmin {

    private final CategoryService categoryService;

    @PostMapping
    public Category create(@RequestBody @Valid Category category) {
        return categoryService.create(category);
    }

    @PatchMapping("/{categoryId}")
    public Category update(@PathVariable("categoryId") int categoryId,
                           @RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.update(categoryDto, categoryId);
    }

    @DeleteMapping("/{categoryId}")
    public void delete(@PathVariable("categoryId") int categoryId) {
        categoryService.delete(categoryId);
    }


}
