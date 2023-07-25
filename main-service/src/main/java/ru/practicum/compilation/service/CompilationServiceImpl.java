package ru.practicum.compilation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.model.*;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventFullDto;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.event.service.StatisticService;
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
    StatisticService statisticService;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Validator.validate(newCompilationDto);

        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        Map<Integer, Event> eventEntityMap = new HashMap<>();
        if (newCompilationDto.getEvents() != null &&
            newCompilationDto.getEvents().size() > 0) {
            List<Event> eventList = eventRepository.findAllByIds(newCompilationDto.getEvents());
            for (Event event : eventList) {
                eventFullDtoList.add(EventMapper.toEventFullDto(event));
                eventEntityMap.put(event.getId(), event);
            }
            eventFullDtoList = statisticService.setViewsToEventFullDto(eventFullDtoList);
        }
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(newCompilationDto, eventFullDtoList);
        Compilation compilation = CompilationMapper.toCompilationEntity(compilationDto);
        for (Event event : compilation.getEvents()) {
            Event eventEntity = eventEntityMap.get(event.getId());
            event.setInitiator(eventEntity.getInitiator());
        }
        Compilation returned = compilationRepository.save(compilation);

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
        Compilation compilationEntity = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation id " + compId + " not found")
        );

        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        Map<Integer, Event> eventEntityMap = new HashMap<>();
        if (updateCompilationRequest.getEvents() != null &&
            updateCompilationRequest.getEvents().size() > 0) {
            List<Event> eventList = eventRepository.findAllByIds(updateCompilationRequest.getEvents());
            for (Event event : eventList) {
                eventFullDtoList.add(EventMapper.toEventFullDto(event));
                eventEntityMap.put(event.getId(), event);
            }
            eventFullDtoList = statisticService.setViewsToEventFullDto(eventFullDtoList);
        } else if (compilationEntity.getEvents() != null &&
                    compilationEntity.getEvents().size() > 0) {
            for (Event event : compilationEntity.getEvents()) {
                eventFullDtoList.add(EventMapper.toEventFullDto(event));
            }
            eventFullDtoList = statisticService.setViewsToEventFullDto(eventFullDtoList);
        }

        CompilationDto updated = CompilationMapper.toCompilationDto(
                CompilationMapper.toCompilationDto(compilationEntity),
                updateCompilationRequest,
                eventFullDtoList
        );

        Validator.validate(updated);

        Compilation compilation = CompilationMapper.toCompilationEntity(updated);
        for (Event event : compilation.getEvents()) {
            Event eventEntity = eventEntityMap.get(event.getId());
            event.setInitiator(eventEntity.getInitiator());
        }
        Compilation returned = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(returned);
    }

    @Override
    public List<CompilationDto> findCompilations(Boolean pinned, int from, int size) {
        List<Compilation> allCompilations = compilationRepository.findAll();
        List<Compilation> compilations = new ArrayList<>();
        List<CompilationDto> compilationDtoList = new ArrayList<>();
        if (pinned != null) {
            for (Compilation compilation : allCompilations) {
                if (compilation.getPinned() == pinned) {
                    compilations.add(compilation);
                }
            }
        } else {
            compilations = allCompilations;
        }

        compilations = compilations.subList(from, Math.min(from + size, compilations.size()));

        for (Compilation compilation : compilations) {
            compilationDtoList.add(CompilationMapper.toCompilationDto(compilation));
        }
        for (CompilationDto compilationDto : compilationDtoList) {
            if (compilationDto.getEvents() != null &&
                compilationDto.getEvents().size() > 0) {
                compilationDto.setEvents(statisticService.setViewsToEventFullDto(compilationDto.getEvents()));
            }
        }

        return compilationDtoList;
    }

    @Override
    public CompilationDto findCompilationById(int compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation id " + compId + " not found")
        );

        return CompilationMapper.toCompilationDto(compilation);
    }
}
