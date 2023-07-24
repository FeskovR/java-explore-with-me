package ru.practicum.compilation.service;

import ru.practicum.compilation.model.CompilationDto;
import ru.practicum.compilation.model.NewCompilationDto;
import ru.practicum.compilation.model.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    //Admin

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(int compId);

    CompilationDto updateCompilation(int compId, UpdateCompilationRequest updateCompilationRequest);

    //Public

    List<CompilationDto> findCompilations(Boolean pinned,
                                          int from,
                                          int size);

    CompilationDto findCompilationById(int compId);
}
