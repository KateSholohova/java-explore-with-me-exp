package ru.practicum.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDto create(CategoryDto categoryDto) {
        log.info("BBBBBB{}", categoryDto.getName().isBlank());
        Category category = CategoryMapper.toCategory(categoryDto);
        categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(category);
    }

    public void delete(int categoryId) {
        if(categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
        } else {
            throw new NotFoundException("Category not found");
        }
    }

    public Category update( CategoryDto categoryDto, int categoryId) {
        if(categoryRepository.existsById(categoryId)) {
            Category category = CategoryMapper.toCategory(categoryDto);
            category.setId(categoryId);
            categoryRepository.save(category);
            return category;
        } else {
            throw new NotFoundException("Category not found");
        }
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public Category findById(int categoryId) {
        if(categoryRepository.existsById(categoryId)) {
            return (categoryRepository.findById(categoryId).get());
        } else {
            throw new NotFoundException("Category not found");
        }
    }

}
