package ru.practicum.category.model;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoryMapper {
    public CategoryDto toCategoryDto(NewCategoryDto newCategoryDto) {
        return new CategoryDto(0, newCategoryDto.getName());
    }
}
