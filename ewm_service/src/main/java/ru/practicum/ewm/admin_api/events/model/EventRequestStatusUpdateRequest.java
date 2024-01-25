package ru.practicum.ewm.admin_api.events.model;

import lombok.Getter;
import ru.practicum.ewm.private_api.requests.model.StatusRequestAction;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class EventRequestStatusUpdateRequest {

    @NotNull
    private List<Long> requestIds;

    @NotNull
    private StatusRequestAction status;
}
