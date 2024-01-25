package ru.practicum.ewm.admin_api.events.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.Constants.DATA_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotBlank(message = "Annotation is not be empty.")
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private long category;

    @NotBlank(message = "Description is not be empty.")
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @JsonFormat(pattern = DATA_PATTERN)
    private LocalDateTime eventDate;

    @NotNull
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;

    @NotBlank(message = "Title is not be empty.")
    @Size(min = 3, max = 120)
    private String title;
}
