package ru.practicum.ewm.admin_api.events;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.EndpointHit;
import ru.practicum.ewm.StatsClient;
import ru.practicum.ewm.ViewStats;
import ru.practicum.ewm.admin_api.categories.CategoriesRepository;
import ru.practicum.ewm.admin_api.categories.model.Category;
import ru.practicum.ewm.admin_api.events.model.Event;
import ru.practicum.ewm.admin_api.events.model.EventFullDto;
import ru.practicum.ewm.admin_api.events.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.admin_api.events.model.EventRequestStatusUpdateResult;
import ru.practicum.ewm.admin_api.events.model.EventShortDto;
import ru.practicum.ewm.admin_api.events.model.NewEventDto;
import ru.practicum.ewm.admin_api.events.model.UpdateEventAdminRequest;
import ru.practicum.ewm.admin_api.events.model.UpdateEventUserRequest;
import ru.practicum.ewm.admin_api.events.model.enums.SortValues;
import ru.practicum.ewm.admin_api.events.model.enums.StateAction;
import ru.practicum.ewm.admin_api.events.model.enums.StateActionAdmin;
import ru.practicum.ewm.admin_api.events.model.enums.StateEvent;
import ru.practicum.ewm.admin_api.users.UserRepository;
import ru.practicum.ewm.admin_api.users.model.User;
import ru.practicum.ewm.exeptions.ConflictDataException;
import ru.practicum.ewm.exeptions.NotFoundException;
import ru.practicum.ewm.exeptions.ValidationException;
import ru.practicum.ewm.private_api.requests.RequestMapper;
import ru.practicum.ewm.private_api.requests.RequestsRepository;
import ru.practicum.ewm.private_api.requests.model.ParticipationRequestDto;
import ru.practicum.ewm.private_api.requests.model.Request;
import ru.practicum.ewm.private_api.requests.model.StatusRequest;
import ru.practicum.ewm.private_api.requests.model.StatusRequestAction;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.Constants.FORMATTER;
import static ru.practicum.ewm.Constants.SORT_BY_EVENT_DATE;
import static ru.practicum.ewm.Constants.SORT_BY_VIEWS;

@Service
@RequiredArgsConstructor
public class EventsService {
    private final EventsRepository eventsRepository;
    private final EventsMapper eventsMapper;
    private final CategoriesRepository categoriesRepository;
    private final UserRepository userRepository;
    private final RequestsRepository requestsRepository;
    private final RequestMapper requestMapper;
    private final StatsClient statsClient;
    private final ObjectMapper objectMapper;


    public EventFullDto updateEventAndStatus(long eventId, UpdateEventAdminRequest updateDto) {
        int delay = 1;
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found: " + eventId));
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(delay))) {
            throw new ConflictDataException("The time for which the event is scheduled cannot be earlier than " + delay
                    + " hours from the current moment: " + event.getEventDate());
        }
        if (updateDto.getEventDate() != null && updateDto.getEventDate().isBefore(LocalDateTime.now().plusHours(delay))) {
            throw new ValidationException("The time for which the event is scheduled cannot be earlier than " + delay
                    + " hours from the current moment: " + updateDto.getEventDate());
        }
        if (updateDto.getStateAction() != null && updateDto.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)
                && !event.getState().equals(StateEvent.PENDING)) {
            throw new ConflictDataException("Event can be published only if it in the waiting moderation state. Event: "
                    + event.getId() + " has state: " + event.getState());
        }
        if (updateDto.getStateAction() != null && updateDto.getStateAction().equals(StateActionAdmin.REJECT_EVENT)
                && event.getState().equals(StateEvent.PUBLISHED)) {
            throw new ConflictDataException("Event can be rejected only if it is not already published. Event: "
                    + event.getId() + " has state: " + event.getState());
        }
        if (updateDto.getAnnotation() != null) event.setAnnotation(updateDto.getAnnotation());
        if (updateDto.getCategory() != null && event.getCategory().getId() != updateDto.getCategory()) {
            Category category = categoriesRepository.findById(updateDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found: " + eventId));
            event.setCategory(category);
        }
        if (updateDto.getDescription() != null) event.setDescription(updateDto.getDescription());
        if (updateDto.getEventDate() != null) event.setEventDate(updateDto.getEventDate());
        if (updateDto.getLocation() != null) event.setLocation(updateDto.getLocation());
        if (updateDto.getPaid() != null) event.setPaid(updateDto.getPaid());
        if (updateDto.getParticipantLimit() != null) event.setParticipantLimit(updateDto.getParticipantLimit());
        if (updateDto.getRequestModeration() != null) event.setRequestModeration(updateDto.getRequestModeration());
        if (updateDto.getTitle() != null) event.setTitle(updateDto.getTitle());
        if (updateDto.getStateAction() != null) {
            if (updateDto.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)) {
                event.setState(StateEvent.PUBLISHED);
                event.setPublishedOn(LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER));
            } else {
                event.setState(StateEvent.CANCELED);
            }
        }
        event.setViews(getStatistic(event, false));

        return eventsMapper.toEventFullDto(eventsRepository.save(event));
    }

    public List<EventFullDto> getAllByParam(List<Long> users, List<StateEvent> states, List<Long> categories,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        return eventsRepository.findAllByParam(users, states, categories, rangeStart,
                        rangeEnd, PageRequest.of(from / size, size)).stream()
                .peek(e -> e.setViews(getStatistic(e, false)))
                .map(eventsMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    public EventFullDto create(long userId, NewEventDto newEventDto) {
        int delay = 2;
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(delay))) {
            throw new ValidationException("The time for which the event is scheduled cannot be earlier than " + delay +
                    " hours from the current moment: " + newEventDto.getEventDate());
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
        Event event = eventsMapper.toEvent(newEventDto);
        Category category = categoriesRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found: " + newEventDto.getCategory()));
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER));
        event.setCategory(category);
        event.setState(StateEvent.PENDING);
        return eventsMapper.toEventFullDto(eventsRepository.save(event));
    }

    public EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateDto) {
        int delay = 2;
        if (updateDto.getEventDate() != null && updateDto.getEventDate().isBefore(LocalDateTime.now().plusHours(delay))) {
            throw new ValidationException("The time for which the event is scheduled cannot be earlier than " + delay
                    + " hours from the current moment: " + updateDto.getEventDate());
        }
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found: " + eventId));

        if (event.getState().equals(StateEvent.PUBLISHED)) {
            throw new ConflictDataException("Only canceled events or events in the waiting moderation state can be " +
                    "modified. Event: " + event.getId() + " has state: " + event.getState());
        }
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictDataException("Only its initiator can change an event: " + event.getInitiator());
        }

        if (updateDto.getAnnotation() != null) event.setAnnotation(updateDto.getAnnotation());
        if (updateDto.getCategory() != null && event.getCategory().getId() != updateDto.getCategory()) {
            Category category = categoriesRepository.findById(updateDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found: " + eventId));
            event.setCategory(category);
        }
        if (updateDto.getDescription() != null) event.setDescription(updateDto.getDescription());
        if (updateDto.getEventDate() != null) event.setEventDate(updateDto.getEventDate());
        if (updateDto.getLocation() != null) event.setLocation(updateDto.getLocation());
        if (updateDto.getPaid() != null) event.setPaid(updateDto.getPaid());
        if (updateDto.getParticipantLimit() != null) event.setParticipantLimit(updateDto.getParticipantLimit());
        if (updateDto.getRequestModeration() != null) event.setRequestModeration(updateDto.getRequestModeration());
        if (updateDto.getStateAction() != null) {
            if (updateDto.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(StateEvent.CANCELED);
            } else {
                event.setState(StateEvent.PENDING);
            }
        }
        if (updateDto.getTitle() != null) event.setTitle(updateDto.getTitle());
        event.setViews(getStatistic(event, false));

        return eventsMapper.toEventFullDto(eventsRepository.save(event));
    }

    public EventRequestStatusUpdateResult updateRequests(long userId, long eventId, EventRequestStatusUpdateRequest updateDto) {
        List<Request> requests = requestsRepository.findAllById(updateDto.getRequestIds());
        if (requests.stream().anyMatch(request -> !request.getStatus().equals(StatusRequest.PENDING))) {
            throw new ConflictDataException("Status can only be changed for pending applications");
        }

        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found: " + eventId));
        event.setViews(getStatistic(event, false));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictDataException("Only its initiator can change an event: " + event.getInitiator());
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequest() >= event.getParticipantLimit()
                && updateDto.getStatus().equals(StatusRequestAction.CONFIRMED)) {
            throw new ConflictDataException("Membership request limit reached: " + event.getParticipantLimit());
        }

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        if (updateDto.getStatus().equals(StatusRequestAction.CONFIRMED)) {
            for (Request request : requests) {
                if (event.getParticipantLimit() == 0 || event.getRequestModeration().equals(false)) {
                    request.setStatus(StatusRequest.CONFIRMED);
                    event.setConfirmedRequest(event.getConfirmedRequest() + 1);
                    request.setEvent(event);
                    requestsRepository.save(request);
                    confirmedRequests.add(requestMapper.toDto(request));

                } else if (event.getConfirmedRequest() < event.getParticipantLimit()) {
                    request.setStatus(StatusRequest.CONFIRMED);
                    event.setConfirmedRequest(event.getConfirmedRequest() + 1);
                    request.setEvent(event);
                    requestsRepository.save(request);
                    confirmedRequests.add(requestMapper.toDto(request));
                } else {
                    request.setStatus(StatusRequest.REJECTED);
                    requestsRepository.save(request);
                    rejectedRequests.add(requestMapper.toDto(request));
                }
            }
        } else if (updateDto.getStatus().equals(StatusRequestAction.REJECTED)) {
            for (Request request : requests) {
                request.setStatus(StatusRequest.REJECTED);
                requestsRepository.save(request);
                rejectedRequests.add(requestMapper.toDto(request));
            }
        }
        result.setConfirmedRequests(confirmedRequests);
        result.setRejectedRequests(rejectedRequests);
        eventsRepository.save(event);
        return result;
    }

    public List<EventShortDto> getAllByInitiator(long userId, int from, int size) {
        return eventsRepository.findAllByInitiator(userId, PageRequest.of(from / size, size)).stream()
                .peek(e -> e.setViews(getStatistic(e, false)))
                .map(eventsMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto getByIdAndInitiator(long userId, long eventId) {
        Event event = eventsRepository.findByIdAndInitiator(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));
        event.setViews(getStatistic(event, false));
        return eventsMapper.toEventFullDto(event);
    }

    public List<ParticipationRequestDto> getAllRequestsByEventAndInitiator(long userId, long eventId) {
        return requestsRepository.findAllByEventIdAndInitiatorId(userId, eventId).stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<EventShortDto> getAllByFilter(HttpServletRequest request, String text, List<Long> categories, Boolean paid,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                              SortValues sort, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size);

        List<Event> events;

        if (sort != null && sort.equals(SortValues.VIEWS)) {
            pageRequest = PageRequest.of(from, size, SORT_BY_VIEWS);
        } else if (sort != null && sort.equals(SortValues.EVENT_DATE)) {
            pageRequest = PageRequest.of(from, size, SORT_BY_EVENT_DATE);
        }

        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new ValidationException("RangeEnd not can be before rangeStart. RangeStart: " + rangeStart + " rangeEnd: " + rangeEnd);
        }

        if (rangeStart == null) rangeStart = LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER);

        if (onlyAvailable) {
            events = eventsRepository.findAllByFilterIsAvailable(text, categories, paid, rangeStart, rangeEnd, pageRequest);
        } else {
            events = eventsRepository.findAllByFilterNotAvailable(text, categories, paid, rangeStart, rangeEnd, pageRequest);
        }

        sendStatistic(request.getRemoteAddr(), request.getRequestURI());

        return events.stream().map(eventsMapper::toEventShortDto).collect(Collectors.toList());
    }

    public EventFullDto getByIdOnlyPublic(HttpServletRequest request, long eventId) {
        Event event = eventsRepository.findByIdOnlyPublic(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));
        sendStatistic(request.getRemoteAddr(), request.getRequestURI());
        event.setViews(getStatistic(event, true));
        return eventsMapper.toEventFullDto(event);
    }

    private void sendStatistic(String ip, String uri) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp("ewm-service");
        endpointHit.setUri(uri);
        endpointHit.setIp(ip);
        endpointHit.setTimestamp(LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER));
        statsClient.create(endpointHit);
    }

    private int getStatistic(Event event, boolean unique) {
        if (event.getState().equals(StateEvent.PUBLISHED)) {
            ResponseEntity<Object> response = statsClient.getAll(event.getPublishedOn(), LocalDateTime.now(),
                    List.of("/events/" + event.getId()), unique);
            List<ViewStats> viewStatsList = objectMapper.convertValue(response.getBody(), new TypeReference<>() {
            });
            if (!viewStatsList.isEmpty() && viewStatsList.get(0).getHits() != null)
                return viewStatsList.get(0).getHits();
        }
        return 0;
    }
}
