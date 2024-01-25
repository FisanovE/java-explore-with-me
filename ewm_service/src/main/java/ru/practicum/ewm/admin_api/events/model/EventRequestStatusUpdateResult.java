package ru.practicum.ewm.admin_api.events.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.private_api.requests.model.ParticipationRequestDto;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
