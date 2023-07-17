package ru.practicum.util;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.CategoryDto;
import ru.practicum.category.model.NewCategoryDto;
import ru.practicum.error.exception.ValidationException;
import ru.practicum.user.model.NewUserRequest;

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
}
