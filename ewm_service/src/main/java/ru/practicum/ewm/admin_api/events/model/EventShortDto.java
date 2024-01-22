package ru.practicum.ewm.admin_api.events.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.admin_api.categories.model.CategoryDto;
import ru.practicum.ewm.admin_api.users.model.UserShortDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.Constants.DATA_PATTERN;

@Getter
@Setter
@NoArgsConstructor
public class EventShortDto {
    private long id;

    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private CategoryDto category;
    private int confirmedRequests;

    @NotNull
    @JsonFormat(pattern = DATA_PATTERN)
    private LocalDateTime eventDate;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private Boolean paid;

    @Size(min = 3, max = 120)
    private String title;
    private int views;
}
