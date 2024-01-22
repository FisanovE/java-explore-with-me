package ru.practicum.ewm.admin_api.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.admin_api.events.model.EventFullDto;
import ru.practicum.ewm.admin_api.events.model.enums.StateEvent;
import ru.practicum.ewm.admin_api.events.model.UpdateEventAdminRequest;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.Constants.DATA_PATTERN;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventsControllerAdmin {

    private final EventsServiceAdmin eventsServiceAdmin;

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventAndStatus(@PathVariable long eventId,
                                             @RequestBody @Valid UpdateEventAdminRequest updateDto) {
        log.info("Update event {}", eventId);
        return eventsServiceAdmin.updateEventAndStatus(eventId, updateDto);
    }

    @GetMapping
    public List<EventFullDto> getAllByParam(@RequestParam(required = false) List<Long> users,
                                            @RequestParam(required = false) List<StateEvent> states,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = DATA_PATTERN) LocalDateTime rangeStart,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = DATA_PATTERN) LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "0", required = false) int from,
                                            @RequestParam(defaultValue = "10", required = false) int size) {
        log.info("Get events by Param users {} states {} categories {} start {} end {} from {} size {}", users, states,
                categories, rangeStart, rangeEnd, from, size);
        return eventsServiceAdmin.getAllByParam(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
