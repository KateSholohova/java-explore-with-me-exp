package ru.practicum.categories;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Validated
public class CategoryControllerAdmin {

    private final CategoryService categoryService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto create(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryService.create(newCategoryDto);
    }

    @PatchMapping("/{categoryId}")
    public CategoryDto update(@PathVariable("categoryId") int categoryId,
                              @RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryService.update(newCategoryDto, categoryId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{categoryId}")
    public void delete(@PathVariable("categoryId") int categoryId) {
        categoryService.delete(categoryId);
    }


}
