package ru.practicum.ewm.public_api.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.admin_api.events.EventsService;
import ru.practicum.ewm.admin_api.events.model.EventFullDto;
import ru.practicum.ewm.admin_api.events.model.EventShortDto;
import ru.practicum.ewm.admin_api.events.model.enums.SortValues;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.Constants.DATA_PATTERN;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventsControllerPublic {

    private final EventsService eventsService;

    @GetMapping
    public List<EventShortDto> getAllByFilter(HttpServletRequest request,
                                              @RequestParam(required = false) String text,
                                              @RequestParam(required = false) List<Long> categories,
                                              @RequestParam(required = false) Boolean paid,
                                              @RequestParam(required = false) @DateTimeFormat(pattern = DATA_PATTERN) LocalDateTime rangeStart,
                                              @RequestParam(required = false) @DateTimeFormat(pattern = DATA_PATTERN) LocalDateTime rangeEnd,
                                              @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                              @RequestParam(required = false) SortValues sort,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Get events by Filter text {} categories {} paid {} start {} end {} onlyAvailable {} sort {} from {} " +
                "size {}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventsService.getAllByFilter(request, text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getByIdOnlyPublic(@PathVariable long eventId, HttpServletRequest request) {
        log.info("Get Public event {} ", eventId);
        return eventsService.getByIdOnlyPublic(request, eventId);
    }

}
