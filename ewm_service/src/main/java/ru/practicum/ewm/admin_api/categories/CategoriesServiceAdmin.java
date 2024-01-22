package ru.practicum.ewm.admin_api.categories;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.admin_api.categories.model.Category;
import ru.practicum.ewm.admin_api.categories.model.CategoryDto;
import ru.practicum.ewm.admin_api.categories.model.NewCategoryDto;
import ru.practicum.ewm.exeptions.NotFoundException;

@Service
@RequiredArgsConstructor
public class CategoriesServiceAdmin {
    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper categoriesMapper;

    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category category = categoriesMapper.toCategory(newCategoryDto);
        return categoriesMapper.toCategoryDto(categoriesRepository.save(category));
    }

    public CategoryDto update(long id, CategoryDto categoryDto) {
       /* Optional<CategoryDto> categoryByName = categoriesRepository.findByName(categoryDto.getName());
        if (categoryByName.isPresent() && !categoryByName.get().getId().equals(id)) {
            throw new ConflictDataException("Category is already registered: " + categoryDto.getName());
        }*/
        Category category = categoriesRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found: " + id));
        category.setName(categoryDto.getName());
        return categoriesMapper.toCategoryDto(categoriesRepository.save(category));
    }

    public void delete(long id) {
        Category category = categoriesRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found: " + id));
        categoriesRepository.deleteById(id);
    }
}
