package ru.practicum.ewm.admin_api.events.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import ru.practicum.ewm.admin_api.events.model.enums.StateAction;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.Constants.DATA_PATTERN;

@Getter
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @JsonFormat(pattern = DATA_PATTERN)
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;

    @Size(min = 3, max = 120)
    private String title;
}
