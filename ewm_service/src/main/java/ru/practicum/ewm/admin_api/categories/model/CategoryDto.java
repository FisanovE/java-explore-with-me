package ru.practicum.ewm.admin_api.categories.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
public class CategoryDto {
    private long id;

    @NotBlank(message = "Name is not be empty.")
    @Size(min = 1, max = 50)
    private String name;
}
