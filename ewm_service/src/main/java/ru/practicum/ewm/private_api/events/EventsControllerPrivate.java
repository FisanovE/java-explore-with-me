package ru.practicum.ewm.private_api.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.admin_api.events.EventsService;
import ru.practicum.ewm.admin_api.events.model.EventFullDto;
import ru.practicum.ewm.admin_api.events.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.admin_api.events.model.EventRequestStatusUpdateResult;
import ru.practicum.ewm.admin_api.events.model.EventShortDto;
import ru.practicum.ewm.admin_api.events.model.NewEventDto;
import ru.practicum.ewm.admin_api.events.model.UpdateEventUserRequest;
import ru.practicum.ewm.private_api.requests.model.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class EventsControllerPrivate {

    private final EventsService eventsService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EventFullDto create(@PathVariable long userId,
                               @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Create event from user: {}", userId);
        return eventsService.create(userId, newEventDto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable long userId,
                                    @PathVariable long eventId,
                                    @RequestBody @Valid UpdateEventUserRequest updateDto) {
        log.info("Update event: {} from user: {}", eventId, userId);
        return eventsService.updateEvent(userId, eventId, updateDto);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequests(@PathVariable long userId,
                                                         @PathVariable long eventId,
                                                         @RequestBody @Valid EventRequestStatusUpdateRequest updateDto) {
        log.info("Update requests of event: {} from user: {}", eventId, userId);
        return eventsService.updateRequests(userId, eventId, updateDto);
    }

    @GetMapping
    public List<EventShortDto> getAllByInitiator(@PathVariable long userId,
                                                 @RequestParam(defaultValue = "0", required = false) int from,
                                                 @RequestParam(defaultValue = "10", required = false) int size) {
        log.info("Get events by initiator {} from {} size {}", userId, from, size);
        return eventsService.getAllByInitiator(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getByIdAndInitiator(@PathVariable long userId, @PathVariable long eventId) {
        log.info("Get events {} by initiator {}", eventId, userId);
        return eventsService.getByIdAndInitiator(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getAllRequestsByEventAndInitiator(@PathVariable long userId, @PathVariable long eventId) {
        log.info("Get requests event {} by user {}", eventId, userId);
        return eventsService.getAllRequestsByEventAndInitiator(userId, eventId);
    }


}
