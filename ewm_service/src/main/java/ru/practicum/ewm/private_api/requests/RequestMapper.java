package ru.practicum.ewm.private_api.requests;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.private_api.requests.model.ParticipationRequestDto;
import ru.practicum.ewm.private_api.requests.model.Request;

@Component
public class RequestMapper {
    public ParticipationRequestDto toDto(Request request) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(request.getId());
        dto.setCreated(request.getCreated());
        dto.setEvent(request.getEvent().getId());
        dto.setRequester(request.getRequester());
        dto.setStatus(request.getStatus());
        return dto;
    }
}
