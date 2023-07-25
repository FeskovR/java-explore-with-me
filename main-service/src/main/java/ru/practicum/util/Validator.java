package ru.practicum.util;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.NewCategoryDto;
import ru.practicum.compilation.model.CompilationDto;
import ru.practicum.compilation.model.NewCompilationDto;
import ru.practicum.error.exception.ValidationException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.NewEventDto;

import java.time.LocalDateTime;

@UtilityClass
public class Validator {
    public void validate(NewCategoryDto newCategoryDto) {
        if (newCategoryDto.getName() == null ||
            newCategoryDto.getName().isBlank() ||
            newCategoryDto.getName().isEmpty()) {
            throw new ValidationException("Incorrect category name");
        }

        if (newCategoryDto.getName().length() > 50) {
            throw new ValidationException("Incorrect category name length");
        }
    }

    public void validate(Category category) {
        if (category.getName() == null ||
                category.getName().isBlank() ||
                category.getName().isEmpty()) {
            throw new ValidationException("Incorrect category name");
        }

        if (category.getName().length() > 50) {
            throw new ValidationException("Incorrect category name length");
        }
    }

    public void validate(NewEventDto newEventDto) {
        if (newEventDto.getTitle() == null ||
            newEventDto.getTitle().isEmpty() ||
            newEventDto.getTitle().isBlank() ||
            newEventDto.getAnnotation() == null ||
            newEventDto.getAnnotation().isEmpty() ||
            newEventDto.getAnnotation().isBlank() ||
            newEventDto.getCategory() == null ||
            newEventDto.getDescription() == null ||
            newEventDto.getDescription().isEmpty() ||
            newEventDto.getDescription().isBlank() ||
            newEventDto.getEventDate() == null ||
            newEventDto.getLocation() == null) {
            throw new ValidationException("All required fields must not be empty");
        }

        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Event date cannot be earlier than 2 hours");
        }

        if (newEventDto.getAnnotation().length() < 20 ||
            newEventDto.getAnnotation().length() > 2000 ||
            newEventDto.getDescription().length() < 20 ||
            newEventDto.getDescription().length() > 7000 ||
            newEventDto.getTitle().length() < 3 ||
            newEventDto.getTitle().length() > 120) {
            throw new ValidationException("Invalid length of fields");
        }
    }

    public void validate(Event event) {
        if (event.getTitle() == null ||
                event.getTitle().isEmpty() ||
                event.getTitle().isBlank() ||
                event.getAnnotation() == null ||
                event.getAnnotation().isEmpty() ||
                event.getAnnotation().isBlank() ||
                event.getCategory() == null ||
                event.getDescription() == null ||
                event.getDescription().isEmpty() ||
                event.getDescription().isBlank() ||
                event.getEventDate() == null) {
            throw new ValidationException("All required fields must not be empty");
        }

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Event date cannot be earlier than 2 hours");
        }

        if (event.getAnnotation().length() < 20 ||
                event.getAnnotation().length() > 2000 ||
                event.getDescription().length() < 20 ||
                event.getDescription().length() > 7000 ||
                event.getTitle().length() < 3 ||
                event.getTitle().length() > 120) {
            throw new ValidationException("Invalid length of fields");
        }
    }

    public void validate(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getTitle() == null ||
            newCompilationDto.getTitle().isBlank() ||
            newCompilationDto.getTitle().isEmpty()) {
            throw new ValidationException("Invalid compilation title");
        }

        if (newCompilationDto.getTitle().length() > 50) {
            throw new ValidationException("Invalid compilation title length");
        }
    }

    public void validate(CompilationDto compilationDto) {
        if (compilationDto.getTitle() == null ||
                compilationDto.getTitle().isBlank() ||
                compilationDto.getTitle().isEmpty()) {
            throw new ValidationException("Invalid compilation title");
        }

        if (compilationDto.getTitle().length() > 50) {
            throw new ValidationException("Invalid compilation title length");
        }
    }
}
