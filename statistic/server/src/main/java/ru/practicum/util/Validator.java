package ru.practicum.util;

import lombok.experimental.UtilityClass;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.HitDto;

import java.time.LocalDateTime;

@UtilityClass
public class Validator {
    public void validate(HitDto hitDto) {
        if (hitDto.getApp() == null)
            throw new ValidationException("App cannot be null");

        if (hitDto.getUri() == null)
            throw new ValidationException("Uri cannot be null");

        if (hitDto.getIp() == null)
            throw new ValidationException("IP cannot be null");

        if (hitDto.getTimestamp() == null)
            throw new ValidationException("Time cannot be null");

        if (hitDto.getTimestamp().isAfter(LocalDateTime.now()))
            throw new ValidationException("Time cannot be after then now");
    }
}
