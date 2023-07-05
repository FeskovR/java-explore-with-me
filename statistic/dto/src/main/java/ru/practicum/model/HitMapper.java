package ru.practicum.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {
    public static Hit toHit(HitDto hitDto) {
        Hit hit = new Hit();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(hitDto.getTimestamp(), format);

        hit.setUri(hitDto.getUri());
        hit.setTimestamp(dateTime);
        hit.setIp(hitDto.getIp());
        hit.setApp(hitDto.getApp());

        return hit;
    }
}
