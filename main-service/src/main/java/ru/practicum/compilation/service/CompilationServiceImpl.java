package ru.practicum.compilation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.model.*;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.event.model.EventEntity;
import ru.practicum.event.model.EventFullDto;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.Statistic;
import ru.practicum.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    CompilationRepository compilationRepository;
    EventRepository eventRepository;
    UserRepository userRepository;
    Statistic statistic;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Validator.validate(newCompilationDto);

        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        Map<Integer, EventEntity> eventEntityMap = new HashMap<>();
        if (newCompilationDto.getEvents() != null &&
            newCompilationDto.getEvents().size() > 0) {
            List<EventEntity> eventEntityList = eventRepository.findAllByIds(newCompilationDto.getEvents());
            for (EventEntity eventEntity : eventEntityList) {
                eventFullDtoList.add(EventMapper.toEventFullDto(eventEntity));
                eventEntityMap.put(eventEntity.getId(), eventEntity);
            }
            eventFullDtoList = statistic.setViewsToEventFullDto(eventFullDtoList);
        }
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(newCompilationDto, eventFullDtoList);
        CompilationEntity compilationEntity = CompilationMapper.toCompilationEntity(compilationDto);
        for (EventEntity event : compilationEntity.getEvents()) {
            EventEntity eventEntity = eventEntityMap.get(event.getId());
            event.setInitiator(eventEntity.getInitiator());
        }
        CompilationEntity returned = compilationRepository.save(compilationEntity);

        return CompilationMapper.toCompilationDto(returned);
    }

    @Override
    public void deleteCompilation(int compId) {
        compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation id " + compId + " not found")
        );

        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(int compId, UpdateCompilationRequest updateCompilationRequest) {
        CompilationEntity compilationEntity = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation id " + compId + " not found")
        );

        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        Map<Integer, EventEntity> eventEntityMap = new HashMap<>();
        if (updateCompilationRequest.getEvents() != null &&
            updateCompilationRequest.getEvents().size() > 0) {
            List<EventEntity> eventEntityList = eventRepository.findAllByIds(updateCompilationRequest.getEvents());
            for (EventEntity eventEntity : eventEntityList) {
                eventFullDtoList.add(EventMapper.toEventFullDto(eventEntity));
                eventEntityMap.put(eventEntity.getId(), eventEntity);
            }
            eventFullDtoList = statistic.setViewsToEventFullDto(eventFullDtoList);
        } else if (compilationEntity.getEvents() != null &&
                    compilationEntity.getEvents().size() > 0) {
            for (EventEntity event : compilationEntity.getEvents()) {
                eventFullDtoList.add(EventMapper.toEventFullDto(event));
            }
            eventFullDtoList = statistic.setViewsToEventFullDto(eventFullDtoList);
        }

        CompilationDto updated = CompilationMapper.toCompilationDto(
                CompilationMapper.toCompilationDto(compilationEntity),
                updateCompilationRequest,
                eventFullDtoList
        );

        Validator.validate(updated);

        CompilationEntity compilation = CompilationMapper.toCompilationEntity(updated);
        for (EventEntity event : compilation.getEvents()) {
            EventEntity eventEntity = eventEntityMap.get(event.getId());
            event.setInitiator(eventEntity.getInitiator());
        }
        CompilationEntity returned = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(returned);
    }

    @Override
    public List<CompilationDto> findCompilations(Boolean pinned, int from, int size) {
        List<CompilationEntity> allCompilations = compilationRepository.findAll();
        List<CompilationEntity> compilations = new ArrayList<>();
        List<CompilationDto> compilationDtoList = new ArrayList<>();
        if (pinned != null) {
            for (CompilationEntity compilation : allCompilations) {
                if (compilation.getPinned() == pinned) {
                    compilations.add(compilation);
                }
            }
        } else {
            compilations = allCompilations;
        }

        compilations = compilations.subList(from, Math.min(from + size, compilations.size()));

        for (CompilationEntity compilation : compilations) {
            compilationDtoList.add(CompilationMapper.toCompilationDto(compilation));
        }
        for (CompilationDto compilationDto : compilationDtoList) {
            if (compilationDto.getEvents() != null &&
                compilationDto.getEvents().size() > 0) {
                compilationDto.setEvents(statistic.setViewsToEventFullDto(compilationDto.getEvents()));
            }
        }

        return compilationDtoList;
    }

    @Override
    public CompilationDto findCompilationById(int compId) {
        CompilationEntity compilationEntity = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation id " + compId + " not found")
        );

        return CompilationMapper.toCompilationDto(compilationEntity);
    }
}
