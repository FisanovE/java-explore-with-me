package ru.practicum.ewm.admin_api.events;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.admin_api.categories.CategoriesMapper;
import ru.practicum.ewm.admin_api.events.model.Event;
import ru.practicum.ewm.admin_api.events.model.EventFullDto;
import ru.practicum.ewm.admin_api.events.model.EventShortDto;
import ru.practicum.ewm.admin_api.events.model.NewEventDto;
import ru.practicum.ewm.admin_api.users.UserMapper;

@Component
@RequiredArgsConstructor
public class EventsMapper {

    private final CategoriesMapper categoriesMapper;
    private final UserMapper userMapper;

    public Event toEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setLocation(newEventDto.getLocation());
        if (newEventDto.getPaid() != null) event.setPaid(newEventDto.getPaid());
        if (newEventDto.getParticipantLimit() != null) event.setParticipantLimit(newEventDto.getParticipantLimit());
        if (newEventDto.getRequestModeration() != null) event.setRequestModeration(newEventDto.getRequestModeration());
        event.setTitle(newEventDto.getTitle());
        return event;
    }

    public EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(categoriesMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setConfirmedRequests(event.getConfirmedRequest());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setInitiator(userMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(event.getViews());
        return eventShortDto;
    }

    public EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(categoriesMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setConfirmedRequests(event.getConfirmedRequest());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setInitiator(userMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());
        return eventFullDto;
    }
}
