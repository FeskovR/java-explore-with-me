package ru.practicum.util;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.CategoryDto;
import ru.practicum.category.model.NewCategoryDto;
import ru.practicum.error.exception.ValidationException;

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
}
