package ru.practicum.ewm.private_api.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exeptions.ConflictDataException;
import ru.practicum.ewm.exeptions.NotFoundException;
import ru.practicum.ewm.admin_api.events.model.Event;
import ru.practicum.ewm.admin_api.events.EventsRepository;
import ru.practicum.ewm.admin_api.events.model.enums.StateEvent;
import ru.practicum.ewm.private_api.requests.model.ParticipationRequestDto;
import ru.practicum.ewm.private_api.requests.model.Request;
import ru.practicum.ewm.private_api.requests.model.StatusRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestsService {
    private final EventsRepository eventsRepository;
    private final RequestsRepository requestsRepository;
    private final RequestMapper requestMapper;

    public ParticipationRequestDto create(long userId, long eventId) {
        List<Request> list = requestsRepository.findAllByEventId(eventId);
        for (Request request : list) {
            if (request.getRequester() == userId)
                throw new ConflictDataException("Request is already registered: " + request.getId());
        }

        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));
        if (event.getInitiator().getId() == userId) throw new ConflictDataException("Requester ID: " + userId
                + " not be equal to Initiator ID: " + event.getInitiator().getId());
        if (!event.getState().equals(StateEvent.PUBLISHED)) throw new ConflictDataException("Status event is: "
                + event.getState());
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequest() >= event.getParticipantLimit())
            throw new ConflictDataException("Membership request limit reached: " + event.getParticipantLimit());

        Request request = new Request();
        request.setRequester(userId);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());
        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            request.setStatus(StatusRequest.PENDING);
        } else {
            request.setStatus(StatusRequest.CONFIRMED);
            event.setConfirmedRequest(event.getConfirmedRequest() + 1);
            eventsRepository.save(event);
        }
        return requestMapper.toDto(requestsRepository.save(request));
    }

    public ParticipationRequestDto updateCancel(long userId, long requestId) {
        Request request = requestsRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found: " + requestId));
        if (request.getRequester() != userId) throw new ConflictDataException("User ID: " + userId
                + " not equal to Requester ID: " + request.getRequester());
        request.setStatus(StatusRequest.CANCELED);
        return requestMapper.toDto(requestsRepository.save(request));
    }

    public List<ParticipationRequestDto> getAllByUserId(long userId) {
        return requestsRepository.findAllByUserId(userId).stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }
}
