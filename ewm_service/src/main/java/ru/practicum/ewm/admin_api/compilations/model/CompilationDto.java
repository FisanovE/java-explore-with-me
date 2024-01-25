package ru.practicum.ewm.admin_api.compilations.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.admin_api.events.model.EventShortDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CompilationDto {

    @NotNull
    private long id;
    private List<EventShortDto> events;

    @NotNull
    private Boolean pinned;

    @NotNull
    @Size(min = 1, max = 50)
    private String title;
}
