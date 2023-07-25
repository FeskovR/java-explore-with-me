package ru.practicum.category.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.service.CategoryService;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.NewCategoryDto;

@RestController
@RequestMapping("/admin/categories")
@Slf4j
@AllArgsConstructor
public class AdminCategoryController {
    CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody NewCategoryDto newCategory) {
        log.info("Adding new category name {}", newCategory.getName());
        return categoryService.addCategory(newCategory);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int catId) {
        log.info("Deleting category id {}", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public Category updateCategory(@PathVariable int catId,
                                   @RequestBody Category updatedCategory) {
        log.info("Updating category id {}", catId);
        return categoryService.updateCategory(catId, updatedCategory);
    }
}
