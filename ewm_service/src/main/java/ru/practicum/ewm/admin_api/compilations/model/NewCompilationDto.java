package ru.practicum.ewm.admin_api.compilations.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned = false;

    @NotBlank(message = "Title is not be empty.")
    @Size(min = 1, max = 50)
    private String title;
}
