package ru.practicum.ewm.admin_api.compilations;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.admin_api.compilations.model.Compilation;
import ru.practicum.ewm.admin_api.compilations.model.CompilationDto;
import ru.practicum.ewm.admin_api.compilations.model.NewCompilationDto;
import ru.practicum.ewm.admin_api.compilations.model.UpdateCompilationRequest;
import ru.practicum.ewm.admin_api.events.EventsMapper;
import ru.practicum.ewm.admin_api.events.EventsRepository;
import ru.practicum.ewm.admin_api.events.model.Event;
import ru.practicum.ewm.admin_api.events.model.EventShortDto;
import ru.practicum.ewm.exeptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationsService {
    private final CompilationsRepository compilationsRepository;
    private final EventsRepository eventsRepository;
    private final CompilationsMapper compilationsMapper;
    private final EventsMapper eventsMapper;

    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationsMapper.toCompilation(newCompilationDto);
        List<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            events = eventsRepository.findAllById(newCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        CompilationDto compilationDto = compilationsMapper.toCompilationDto(compilationsRepository.save(compilation));
        if (newCompilationDto.getEvents() != null) {
            compilationDto.setEvents(events.stream()
                    .map(eventsMapper::toEventShortDto)
                    .collect(Collectors.toList()));
        }
        return compilationDto;
    }

    public CompilationDto update(long id, UpdateCompilationRequest updateCompilationRequest) {
        List<Event> events = new ArrayList<>();
        Compilation compilation = compilationsRepository.findById(id).orElseThrow(() -> new NotFoundException("Compilation not found: " + id));
        if (updateCompilationRequest.getEvents() != null) {
            events = eventsRepository.findAllById(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) compilation.setPinned(updateCompilationRequest.getPinned());
        if (updateCompilationRequest.getTitle() != null) compilation.setTitle(updateCompilationRequest.getTitle());
        CompilationDto compilationDto = compilationsMapper.toCompilationDto(compilationsRepository.save(compilation));
        compilationDto.setEvents(events.stream()
                .map(eventsMapper::toEventShortDto)
                .collect(Collectors.toList()));
        return compilationDto;
    }

    public void delete(long id) {
        compilationsRepository.deleteById(id);
    }

    public CompilationDto getById(long id) {
        Compilation compilation = compilationsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation not found: " + id));
        CompilationDto compilationDto = compilationsMapper.toCompilationDto(compilation);
        compilationDto.setEvents(compilation.getEvents().stream()
                .map(eventsMapper::toEventShortDto)
                .collect(Collectors.toList()));
        return compilationDto;
    }

    public List<CompilationDto> getAllByPinned(boolean pinned, int from, int size) {
        List<Compilation> compilations = compilationsRepository.findAllByPinned(pinned, PageRequest.of(from / size, size));
        List<CompilationDto> compilationDtos = new ArrayList<>();

        for (Compilation compilation : compilations) {
            CompilationDto compilationDto = compilationsMapper.toCompilationDto(compilation);
            compilationDto.setEvents(compilation.getEvents().stream()
                    .map(eventsMapper::toEventShortDto)
                    .collect(Collectors.toList()));
            compilationDtos.add(compilationDto);
        }
        return compilationDtos;
    }

    private List<EventShortDto> getEvents(List<Long> ids) {
        return eventsRepository.findAllById(ids).stream()
                .map(eventsMapper::toEventShortDto)
                .collect(Collectors.toList());
    }
}
