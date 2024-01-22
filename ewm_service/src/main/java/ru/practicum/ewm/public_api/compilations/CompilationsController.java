package ru.practicum.ewm.public_api.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.admin_api.compilations.model.CompilationDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationsController {

    private final CompilationsService compilationsService;

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable long compId) {
        log.info("Get compilation {}", compId);
        return compilationsService.getById(compId);
    }

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(defaultValue = "false", required = false) boolean pinned,
                                       @RequestParam(defaultValue = "0", required = false) int from,
                                       @RequestParam(defaultValue = "10", required = false) int size) {
        log.info("Get all compilation pinned: {} from: {} size: {}", pinned, from, size);
        return compilationsService.getAllByPinned(pinned, from, size);
    }

}
