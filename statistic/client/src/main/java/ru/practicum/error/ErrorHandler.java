package ru.practicum.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice("ru.practicum")
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public ApiError handler(final ValidationException e) {
        return new ApiError(LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST,
                            "Validation exception",
                            e.getMessage());
    }
}
