package ru.practicum.category.service;


import ru.practicum.category.model.CategoryDto;
import ru.practicum.category.model.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(NewCategoryDto categoryDto);

    CategoryDto updateCategory(int catId, CategoryDto categoryDto);

    void deleteCategory(int catId);

    List<CategoryDto> findCategories(int from, int size);

    CategoryDto findCategoryById(int catId);
}
