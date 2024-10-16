package ru.practicum.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.fromNewCategoryDtoToCategory(newCategoryDto);
        categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(category);
    }

    @Transactional
    public void delete(int categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
        } else {
            throw new NotFoundException("Category not found");
        }
    }

    @Transactional
    public CategoryDto update(NewCategoryDto newCategoryDto, int categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            if (categoryRepository.existsByName(newCategoryDto.getName())) {
                Category category = categoryRepository.findByName(newCategoryDto.getName());
                if (category.getId() == categoryId) {
                    return CategoryMapper.toCategoryDto(category);
                }
                throw new ConflictException("Category name already exists");
            } else {
                Category category = CategoryMapper.fromNewCategoryDtoToCategory(newCategoryDto);
                category.setId(categoryId);
                categoryRepository.save(category);
                return CategoryMapper.toCategoryDto(category);
            }

        } else {
            throw new NotFoundException("Category not found");
        }
    }

    @Transactional
    public List<CategoryDto> findAll(int from, int size) {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::toCategoryDto)
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());


    }

    @Transactional
    public CategoryDto findById(int categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            return (CategoryMapper.toCategoryDto(categoryRepository.findById(categoryId).get()));
        } else {
            throw new NotFoundException("Category not found");
        }
    }

}
