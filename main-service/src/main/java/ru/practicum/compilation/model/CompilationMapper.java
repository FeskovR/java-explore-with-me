package ru.practicum.compilation.model;

import lombok.experimental.UtilityClass;
import ru.practicum.event.model.EventEntity;
import ru.practicum.event.model.EventFullDto;
import ru.practicum.event.model.EventMapper;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CompilationMapper {
    public CompilationDto toCompilationDto(NewCompilationDto newCompilationDto,
                                           List<EventFullDto> events) {
        return new CompilationDto(0,
                newCompilationDto.isPinned(),
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

    public CompilationDto toCompilationDto(CompilationEntity compilationEntity) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilationEntity.getId());
        compilationDto.setPinned(compilationEntity.getPinned());
        compilationDto.setTitle(compilationEntity.getTitle());
        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        for (EventEntity event : compilationEntity.getEvents()) {
            eventFullDtoList.add(EventMapper.toEventFullDto(event));
        }
        compilationDto.setEvents(eventFullDtoList);

        return compilationDto;
    }

    public CompilationEntity toCompilationEntity(CompilationDto compilationDto) {
        CompilationEntity compilationEntity = new CompilationEntity();
        compilationEntity.setId(compilationDto.getId());
        compilationEntity.setTitle(compilationDto.getTitle());
        compilationEntity.setPinned(compilationDto.getPinned());
        List<EventEntity> eventEntityList = new ArrayList<>();
        for (EventFullDto event : compilationDto.getEvents()) {
            eventEntityList.add(EventMapper.toEventEntity(event));
        }
        compilationEntity.setEvents(eventEntityList);

        return compilationEntity;
    }
}
