package ru.practicum.compilation.model;

import lombok.experimental.UtilityClass;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventFullDto;
import ru.practicum.event.model.EventMapper;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CompilationMapper {
    public CompilationDto toCompilationDto(NewCompilationDto newCompilationDto,
                                           List<EventFullDto> events) {
        return new CompilationDto(0,
                newCompilationDto.getPinned(),
                newCompilationDto.getTitle(),
                events);
    }

    public CompilationDto toCompilationDto(CompilationDto updatingCompilation,
                                           UpdateCompilationRequest updateCompilationRequest,
                                           List<EventFullDto> events) {
        if (updateCompilationRequest.getTitle() != null) {
            updatingCompilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getPinned() != null) {
            updatingCompilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (events != null) {
            updatingCompilation.setEvents(events);
        }

        return updatingCompilation;
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        for (Event event : compilation.getEvents()) {
            eventFullDtoList.add(EventMapper.toEventFullDto(event));
        }
        compilationDto.setEvents(eventFullDtoList);

        return compilationDto;
    }

    public Compilation toCompilationEntity(CompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        compilation.setId(compilationDto.getId());
        compilation.setTitle(compilationDto.getTitle());
        compilation.setPinned(compilationDto.getPinned());
        List<Event> eventList = new ArrayList<>();
        for (EventFullDto event : compilationDto.getEvents()) {
            eventList.add(EventMapper.toEventEntity(event));
        }
        compilation.setEvents(eventList);

        return compilation;
    }
}
