package ru.practicum.ewm.admin_api.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.admin_api.categories.model.CategoryDto;
import ru.practicum.ewm.admin_api.categories.model.NewCategoryDto;

import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoriesControllerAdmin {

    private final CategoriesService categoriesService;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Create category");
        /*return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriesServiceAdmin.create(newCategoryDto));*/
        return new ResponseEntity<>(categoriesService.create(newCategoryDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> update(@PathVariable long catId,
                                              @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Update category {}", catId);
        return new ResponseEntity<>(categoriesService.update(catId, categoryDto), HttpStatus.OK);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> delete(@PathVariable long catId) {
        log.info("Delete category {}", catId);
        categoriesService.delete(catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
