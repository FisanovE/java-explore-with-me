package ru.practicum.ewm.admin_api.events.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.admin_api.categories.model.CategoryDto;
import ru.practicum.ewm.admin_api.events.model.enums.StateEvent;
import ru.practicum.ewm.admin_api.users.model.UserShortDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.Constants.DATA_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {

    private long id;

    @Size(min = 20, max = 2000)
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;

    @JsonFormat(pattern = DATA_PATTERN)
    private LocalDateTime createdOn;

    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @JsonFormat(pattern = DATA_PATTERN)
    private LocalDateTime eventDate;
    private UserShortDto initiator;

    @NotNull
    private Location location;

    @NotNull
    private Boolean paid;
    private int participantLimit = 0;

    @JsonFormat(pattern = DATA_PATTERN)
    private LocalDateTime publishedOn;
    private Boolean requestModeration = true;
    private StateEvent state;

    @Size(min = 3, max = 120)
    private String title;
    private int views;
}
