package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Hit;
import ru.practicum.model.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Hit, Long> {
    // получение списка запросов по всем uri без уникального ip
    @Query("select new ru.practicum.model.StatDto(h.app, h.uri, count(h.id)) " +
            "from Hit as h "+
            "where h.timestamp > ?1 and h.timestamp < ?2 " +
            "group by h.uri, h.app "+
            "order by count(h.id) desc")
    List<StatDto> getAllHitsCountNotUniqueIp(LocalDateTime start, LocalDateTime end);

    // получение списка запросов по всем uri с уникальным ip
    @Query("select new ru.practicum.model.StatDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit as h "+
            "where h.timestamp > ?1 and h.timestamp < ?2 " +
            "group by h.uri, h.app "+
            "order by count(h.id) desc")
    List<StatDto> getAllHitsCountWithUniqueIp(LocalDateTime start, LocalDateTime end);

    // получение списка запросов по заданным uri без уникального ip
    @Query("select new ru.practicum.model.StatDto(h.app, h.uri, count(h.id)) " +
            "from Hit as h "+
            "where h.timestamp > ?1 and h.timestamp < ?2 and h.uri in ?3 " +
            "group by h.uri, h.app "+
            "order by count(h.id) desc")
    List<StatDto> getUriHitsCountNotUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    // получение списка запросов по заданным uri с уникальным ip
    @Query("select new ru.practicum.model.StatDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit as h "+
            "where h.timestamp > ?1 and h.timestamp < ?2 and h.uri in ?3 " +
            "group by h.uri, h.app "+
            "order by count(h.id) desc")
    List<StatDto> getUriHitsCountWithUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
