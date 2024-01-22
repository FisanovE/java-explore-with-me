package ru.practicum.ewm.public_api.categories;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.admin_api.categories.CategoriesMapper;
import ru.practicum.ewm.admin_api.categories.CategoriesRepository;
import ru.practicum.ewm.admin_api.categories.model.Category;
import ru.practicum.ewm.admin_api.categories.model.CategoryDto;
import ru.practicum.ewm.exeptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper categoriesMapper;

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
