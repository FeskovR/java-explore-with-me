package ru.practicum.util;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.CategoryDto;
import ru.practicum.category.model.NewCategoryDto;
import ru.practicum.compilation.model.CompilationDto;
import ru.practicum.compilation.model.NewCompilationDto;
import ru.practicum.error.exception.ValidationException;
import ru.practicum.event.model.EventEntity;
import ru.practicum.event.model.NewEventDto;
import ru.practicum.user.model.NewUserRequest;

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

    public void validate(CategoryDto categoryDto) {
        if (categoryDto.getName() == null ||
                categoryDto.getName().isBlank() ||
                categoryDto.getName().isEmpty()) {
            throw new ValidationException("Incorrect category name");
        }

        if (categoryDto.getName().length() > 50) {
            throw new ValidationException("Incorrect category name length");
        }
    }

    public void validate(NewUserRequest newUserRequest) {
        if (newUserRequest.getName() == null ||
            newUserRequest.getName().isEmpty() ||
            newUserRequest.getName().isBlank()) {
            throw new ValidationException("User name cannot be empty");
        }

        if (newUserRequest.getEmail() == null ||
            newUserRequest.getEmail().isEmpty() ||
            newUserRequest.getEmail().isBlank() ||
            !newUserRequest.getEmail().contains("@")) {
            throw new ValidationException("Wrong email format");
        }

        if (newUserRequest.getName().length() > 250 ||
            newUserRequest.getName().length() < 2) {
            throw new ValidationException("Incorrect user name length");
        }

        if (newUserRequest.getEmail().length() > 254 ||
            newUserRequest.getEmail().length() < 6) {
            throw new ValidationException("Incorrect user email length");
        }

        String[] email = newUserRequest.getEmail().split("@");
        String emailLocalpart = email[0];
        String emailDomainpart = email[1];

        if (emailLocalpart.length() > 64 || emailDomainpart.length() > 63) {
            throw new ValidationException("Incorrect email format");
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

    public void validate(EventEntity eventEntity) {
        if (eventEntity.getTitle() == null ||
                eventEntity.getTitle().isEmpty() ||
                eventEntity.getTitle().isBlank() ||
                eventEntity.getAnnotation() == null ||
                eventEntity.getAnnotation().isEmpty() ||
                eventEntity.getAnnotation().isBlank() ||
                eventEntity.getCategory() == null ||
                eventEntity.getDescription() == null ||
                eventEntity.getDescription().isEmpty() ||
                eventEntity.getDescription().isBlank() ||
                eventEntity.getEventDate() == null) {
            throw new ValidationException("All required fields must not be empty");
        }

        if (eventEntity.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Event date cannot be earlier than 2 hours");
        }

        if (eventEntity.getAnnotation().length() < 20 ||
                eventEntity.getAnnotation().length() > 2000 ||
                eventEntity.getDescription().length() < 20 ||
                eventEntity.getDescription().length() > 7000 ||
                eventEntity.getTitle().length() < 3 ||
                eventEntity.getTitle().length() > 120) {
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
