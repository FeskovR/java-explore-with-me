package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.HitDto;
import ru.practicum.model.StatDto;
import ru.practicum.service.StatService;

import javax.xml.bind.ValidationException;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class StatController {
    private final StatService statService;

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED) // 201
    public void hit(@RequestBody HitDto hit) {
        log.info("Add hit for event {} from ip: {}", hit.getUri(), hit.getIp());
        statService.hit(hit);
    }

    @GetMapping(path = "/stats")
    public List<StatDto> getStats(@RequestParam String start,
                                  @RequestParam String end,
                                  @RequestParam(required = false) List<String> uris,
                                  @RequestParam(required = false) boolean unique) throws ValidationException {
        log.info("Get stats for {}", uris == null ? "all" : uris.toString());
        return statService.getStats(start, end, uris, unique);
    }
}
