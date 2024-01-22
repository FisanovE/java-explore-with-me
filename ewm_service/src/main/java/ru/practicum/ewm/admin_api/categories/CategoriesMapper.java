package ru.practicum.ewm.admin_api.categories;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.admin_api.categories.model.Category;
import ru.practicum.ewm.admin_api.categories.model.CategoryDto;
import ru.practicum.ewm.admin_api.categories.model.NewCategoryDto;

@Component
public class CategoriesMapper {
    public Category toCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public CategoryDto toCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public Category toCategoryFromDto(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }
}
