package ru.practicum.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.StatClient;
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
public class Statistic {
    private StatClient statClient;

    public Map<Integer, Long> getStat(List<Integer> eventIds,
                                      LocalDateTime start,
                                      LocalDateTime end,
                                      boolean unique) {
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
        List<Integer> eventsIds = new ArrayList<>();
        eventsIds.add(eventFullDto.getId());
        Map<Integer, Long> hits = getStat(eventsIds,
                LocalDateTime.now().minusYears(10),
                LocalDateTime.now(),
                true);
        eventFullDto.setViews(hits.get(eventFullDto.getId()));
        return eventFullDto;
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

    public EventShortDto setViewsToEventShortDto(EventShortDto eventShortDto) {
        List<Integer> eventsIds = new ArrayList<>();
        eventsIds.add(eventShortDto.getId());
        Map<Integer, Long> hits = getStat(eventsIds,
                LocalDateTime.now().minusYears(10),
                LocalDateTime.now(),
                true);
        eventShortDto.setViews(hits.get(eventShortDto.getId()));
        return eventShortDto;
    }
}
