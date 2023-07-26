package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.Hit;
import ru.practicum.model.HitDto;
import ru.practicum.model.HitMapper;
import ru.practicum.model.StatDto;
import ru.practicum.repository.StatRepository;
import ru.practicum.util.Validator;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Override
    public void hit(HitDto hitDto) {
        Validator.validate(hitDto);
        Hit hit = HitMapper.toHit(hitDto);
        statRepository.save(hit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start != null &&
                end != null &&
                end.isBefore(start)) {
            throw new ValidationException("Invalid start-end range");
        }
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
