package ru.practicum.ewm.admin_api.categories.model;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class NewCategoryDto {

    @NotBlank(message = "Name is not be empty.")
    @Size(min = 1, max = 50)
    private String name;
}
