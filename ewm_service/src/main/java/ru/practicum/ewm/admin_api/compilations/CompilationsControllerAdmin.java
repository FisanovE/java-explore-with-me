package ru.practicum.ewm.admin_api.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.admin_api.compilations.model.CompilationDto;
import ru.practicum.ewm.admin_api.compilations.model.NewCompilationDto;
import ru.practicum.ewm.admin_api.compilations.model.UpdateCompilationRequest;

import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationsControllerAdmin {

    private final CompilationsService compilationsService;

    @PostMapping
    public ResponseEntity<CompilationDto> create(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Create compilation");
        return new ResponseEntity<>(compilationsService.create(newCompilationDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> update(@PathVariable long compId,
                                 @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("Update compilation {}", compId);
        return new ResponseEntity<>(compilationsService.update(compId, updateCompilationRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> delete(@PathVariable long compId) {
        log.info("Delete compilation {}", compId);
        compilationsService.delete(compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
