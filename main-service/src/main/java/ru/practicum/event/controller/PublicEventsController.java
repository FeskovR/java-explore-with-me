package ru.practicum.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.enums.EventSort;
import ru.practicum.event.model.EventFullDto;
import ru.practicum.event.model.EventShortDto;
import ru.practicum.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@Slf4j
@AllArgsConstructor
public class PublicEventsController {
    EventService eventService;

    @GetMapping
    public List<EventShortDto> findEvents(@RequestParam String text,
                                          @RequestParam List<Integer> categories,
                                          @RequestParam boolean paid,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                          @RequestParam boolean onlyAvailable,
                                          @RequestParam EventSort sort,
                                          @RequestParam int from,
                                          @RequestParam int size) {
        log.info("Getting public events");
        return eventService.findEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto findEventById(@PathVariable int id) {
        log.info("Getting event by id {}", id);
        return eventService.findEventByIdPublic(id);
    }
}
