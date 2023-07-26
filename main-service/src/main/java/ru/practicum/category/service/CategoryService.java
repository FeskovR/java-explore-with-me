package ru.practicum.category.service;


import ru.practicum.category.model.Category;
import ru.practicum.category.model.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    Category addCategory(NewCategoryDto categoryDto);

    Category updateCategory(int catId, Category category);

    void deleteCategory(int catId);

    List<Category> findCategories(int from, int size);

    Category findCategoryById(int catId);
}
