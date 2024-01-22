package ru.practicum.ewm.public_api.compilations;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.admin_api.compilations.CompilationsMapper;
import ru.practicum.ewm.admin_api.compilations.CompilationsRepository;
import ru.practicum.ewm.admin_api.compilations.model.Compilation;
import ru.practicum.ewm.admin_api.compilations.model.CompilationDto;
import ru.practicum.ewm.admin_api.events.EventsMapper;
import ru.practicum.ewm.admin_api.events.EventsRepository;
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
        /*  return compilations.stream()
                .map(compilation -> {
                    CompilationDto compilationDto = compilationsMapper.toCompilationDto(compilation);
                    List<EventShortDto> events = getEvents(compilation.getEvents());
                    compilationDto.setEvents(events);
                    return compilationDto;
                })
                .collect(Collectors.toList());*/
    }

    private List<EventShortDto> getEvents(List<Long> ids) {
        return eventsRepository.findAllById(ids).stream()
                .map(eventsMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

}
