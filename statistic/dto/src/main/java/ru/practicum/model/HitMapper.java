package ru.practicum.model;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HitMapper {
    public Hit toHit(HitDto hitDto) {
        Hit hit = new Hit();

        hit.setUri(hitDto.getUri());
        hit.setTimestamp(hitDto.getTimestamp());
        hit.setIp(hitDto.getIp());
        hit.setApp(hitDto.getApp());

        return hit;
    }
}
