package ru.practicum.compilation.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.model.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@Slf4j
@AllArgsConstructor
public class PublicCompilationController {
    CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> findCompilations(@RequestParam(required = false) Boolean pinned,
                                                 @RequestParam(required = false, defaultValue = "0") int from,
                                                 @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Getting compilations by params");
        return compilationService.findCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto findCompilationById(@PathVariable int compId) {
        log.info("Getting compilation by id {}", compId);
        return compilationService.findCompilationById(compId);
    }
}
