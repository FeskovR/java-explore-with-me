package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Hit;
import ru.practicum.model.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Hit, Long> {
    // получение списка запросов по всем uri без уникального ip
    @Query("select new ru.practicum.model.StatDto(h.app, h.uri, count(h.id)) " +
            "from Hit as h " +
            "where h.timestamp > :start and h.timestamp < :end " +
            "group by h.uri, h.app " +
            "order by count(h.id) desc")
    List<StatDto> getAllHitsCountNotUniqueIp(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);

    // получение списка запросов по всем uri с уникальным ip
    @Query("select new ru.practicum.model.StatDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit as h " +
            "where h.timestamp > :start and h.timestamp < :end " +
            "group by h.uri, h.app " +
            "order by count(h.id) desc")
    List<StatDto> getAllHitsCountWithUniqueIp(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);

    // получение списка запросов по заданным uri без уникального ip
    @Query("select new ru.practicum.model.StatDto(h.app, h.uri, count(h.id)) " +
            "from Hit as h " +
            "where h.timestamp > :start and h.timestamp < :end and h.uri in :uris " +
            "group by h.uri, h.app " +
            "order by count(h.id) desc")
    List<StatDto> getUriHitsCountNotUniqueIp(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end,
                                             @Param("uris") List<String> uris);

    // получение списка запросов по заданным uri с уникальным ip
    @Query("select new ru.practicum.model.StatDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit as h " +
            "where h.timestamp > :start and h.timestamp < :end and h.uri in :uris " +
            "group by h.uri, h.app " +
            "order by count(h.id) desc")
    List<StatDto> getUriHitsCountWithUniqueIp(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end,
                                              @Param("uris") List<String> uris);
}
