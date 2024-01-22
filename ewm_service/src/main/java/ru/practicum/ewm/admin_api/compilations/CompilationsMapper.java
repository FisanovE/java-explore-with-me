package ru.practicum.ewm.admin_api.compilations;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.admin_api.compilations.model.Compilation;
import ru.practicum.ewm.admin_api.compilations.model.CompilationDto;
import ru.practicum.ewm.admin_api.compilations.model.NewCompilationDto;

@Component
public class CompilationsMapper {
    public Compilation toCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }
}
