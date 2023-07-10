package ru.practicum.service;


import ru.practicum.model.HitDto;
import ru.practicum.model.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void hit(HitDto hit);

    List<StatDto> getStats(LocalDateTime startString,
                           LocalDateTime endString,
                           List<String> uris,
                           boolean unique);
}
