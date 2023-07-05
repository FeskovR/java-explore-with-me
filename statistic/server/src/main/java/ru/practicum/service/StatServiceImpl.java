package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.Hit;
import ru.practicum.model.HitDto;
import ru.practicum.model.HitMapper;
import ru.practicum.model.StatDto;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Override
    public void hit(HitDto hitDto) {
        Hit hit = HitMapper.toHit(hitDto);
        statRepository.save(hit);
    }

    @Override
    public List<StatDto> getStats(String startString, String endString, List<String> uris, boolean unique) {
        // Преобразование даты-времени из строки в LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startString, formatter);
        LocalDateTime end = LocalDateTime.parse(endString, formatter);

        if (uris == null) {
            if (!unique) {
                return statRepository.getAllHitsCountNotUniqueIp(start, end);
            } else {
                return statRepository.getAllHitsCountWithUniqueIp(start, end);
            }
        } else {
            if (!unique) {
                return statRepository.getUriHitsCountNotUniqueIp(start, end, uris);
            } else {
                return statRepository.getUriHitsCountWithUniqueIp(start, end, uris);
            }
        }
    }
}
