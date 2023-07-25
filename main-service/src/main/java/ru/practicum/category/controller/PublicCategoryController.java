package ru.practicum.category.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.service.CategoryService;
import ru.practicum.category.model.Category;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
@AllArgsConstructor
public class PublicCategoryController {
    CategoryService categoryService;

    @GetMapping
    public List<Category> findCategories(@RequestParam(required = false, defaultValue = "0") int from,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Getting categories with params: from {}, size {}", from, size);
        return categoryService.findCategories(from, size);
    }

    @GetMapping("/{catId}")
    public Category findCategoryById(@PathVariable int catId) {
        log.info("Getting category id {}", catId);
        return categoryService.findCategoryById(catId);
    }
}