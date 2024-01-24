package ru.practicum.ewm.admin_api.categories;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.admin_api.categories.model.Category;
import ru.practicum.ewm.admin_api.categories.model.CategoryDto;
import ru.practicum.ewm.admin_api.categories.model.NewCategoryDto;
import ru.practicum.ewm.exeptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper categoriesMapper;

    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category category = categoriesMapper.toCategory(newCategoryDto);
        return categoriesMapper.toCategoryDto(categoriesRepository.save(category));
    }

    public CategoryDto update(long id, CategoryDto categoryDto) {
        Category category = categoriesRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found: " + id));
        category.setName(categoryDto.getName());
        return categoriesMapper.toCategoryDto(categoriesRepository.save(category));
    }

    public void delete(long id) {
        Category category = categoriesRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found: " + id));
        categoriesRepository.deleteById(id);
    }

    public List<CategoryDto> getAll(int from, int size) {
        return categoriesRepository.findAll(PageRequest.of(from / size, size)).stream()
                .map(categoriesMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getById(long id) {
        Category category = categoriesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));
        return categoriesMapper.toCategoryDto(category);
    }
}
