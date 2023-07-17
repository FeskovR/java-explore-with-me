package ru.practicum.error;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.error.exception.ValidationException;

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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ApiError handler(final NotFoundException e) {
        return new ApiError(LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Not found exception",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT) //409
    public ApiError handler(final PSQLException e) {
        return new ApiError(LocalDateTime.now(),
                HttpStatus.CONFLICT,
                "DB Error",
                e.getMessage());
    }
}
