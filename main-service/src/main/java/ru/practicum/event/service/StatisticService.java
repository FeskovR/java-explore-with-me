package ru.practicum.event.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.StatClient;
import ru.practicum.error.exception.ValidationException;
import ru.practicum.event.model.EventFullDto;
import ru.practicum.event.model.EventShortDto;
import ru.practicum.model.StatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class StatisticService {
    private StatClient statClient;

    public Map<Integer, Long> getStat(List<Integer> eventIds,
                                      LocalDateTime start,
                                      LocalDateTime end,
                                      boolean unique) {
        if (start != null && end != null && end.isBefore(start)) {
            throw new ValidationException("Invalid start-end range");
        }
        List<String> uris = new ArrayList<>();
        Map<Integer, Long> hits = new HashMap<>();
        for (Integer eventId : eventIds) {
            uris.add("/events/" + eventId);
            hits.put(eventId, 0L);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<StatDto> statDtos = statClient.getStat(start.format(formatter),
                                                    end.format(formatter),
                                                    uris,
                                                    unique);
        for (StatDto statDto : statDtos) {
            int eventId = Integer.parseInt(statDto.getUri().split("/")[2]);
            long views = statDto.getHits();
            hits.put(eventId, views);
        }

        return hits;
    }

    public List<EventFullDto> setViewsToEventFullDto(List<EventFullDto> events) {
        List<Integer> eventIds = new ArrayList<>();
        for (EventFullDto event : events) {
            eventIds.add(event.getId());
        }
        Map<Integer, Long> hits = getStat(eventIds,
                LocalDateTime.now().minusYears(10),
                LocalDateTime.now(),
                true);
        for (EventFullDto event : events) {
            event.setViews(hits.get(event.getId()));
        }

        return events;
    }

    public EventFullDto setViewsToEventFullDto(EventFullDto eventFullDto) {
        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        eventFullDtoList.add(eventFullDto);
        return setViewsToEventFullDto(eventFullDtoList).get(0);
    }

    public List<EventShortDto> setViewsToEventShortDto(List<EventShortDto> events) {
        List<Integer> eventIds = new ArrayList<>();
        for (EventShortDto event : events) {
            eventIds.add(event.getId());
        }
        Map<Integer, Long> hits = getStat(eventIds,
                LocalDateTime.now().minusYears(10),
                LocalDateTime.now(),
                true);
        for (EventShortDto event : events) {
            event.setViews(hits.get(event.getId()));
        }

        return events;
    }
}
