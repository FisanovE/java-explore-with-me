package ru.practicum.ewm.admin_api.compilations;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.admin_api.compilations.model.Compilation;
import ru.practicum.ewm.admin_api.compilations.model.CompilationDto;
import ru.practicum.ewm.admin_api.compilations.model.NewCompilationDto;
import ru.practicum.ewm.admin_api.compilations.model.UpdateCompilationRequest;
import ru.practicum.ewm.admin_api.events.EventsMapper;
import ru.practicum.ewm.admin_api.events.EventsRepository;
import ru.practicum.ewm.admin_api.events.model.Event;
import ru.practicum.ewm.exeptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationsServiceAdmin {
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
}
