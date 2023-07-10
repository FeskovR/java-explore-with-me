package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.HitDto;
import ru.practicum.model.StatDto;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class StatController {
    private final StatService statService;

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED) // 201
    public void hit(@RequestBody HitDto hit) {
        log.info("Add hit for {} from ip: {}", hit.getUri(), hit.getIp());
        statService.hit(hit);
    }

    @GetMapping(path = "/stats")
    public List<StatDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                  @RequestParam(required = false) List<String> uris,
                                  @RequestParam(required = false) boolean unique) {
        log.info("Get stats for {}", uris == null ? "all" : uris.toString());
        return statService.getStats(start, end, uris, unique);
    }
}
