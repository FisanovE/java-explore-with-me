package ru.practicum.ewm.public_api.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.admin_api.categories.CategoriesService;
import ru.practicum.ewm.admin_api.categories.model.CategoryDto;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoriesControllerPublic {

    private final CategoriesService categoriesService;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0", required = false) int from,
                                    @RequestParam(defaultValue = "10", required = false) int size) {
        log.info("Get all category");
        return categoriesService.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable long catId) {
        log.info("Get category {}", catId);
        return categoriesService.getById(catId);
    }
}
