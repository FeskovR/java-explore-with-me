package ru.practicum.category.model;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoryMapper {
    public Category toCategoryDto(NewCategoryDto newCategoryDto) {
        return new Category(0, newCategoryDto.getName());
    }
}
