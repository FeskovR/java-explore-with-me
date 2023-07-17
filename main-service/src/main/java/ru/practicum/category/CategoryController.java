package ru.practicum.category;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.CategoryDto;
import ru.practicum.category.model.NewCategoryDto;

import java.util.List;

@RestController
@RequestMapping
@Slf4j
@AllArgsConstructor
public class CategoryController {
    CategoryService categoryService;

    // Администраторские методы

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody NewCategoryDto newCategory) {
        log.info("Adding new category name {}", newCategory.getName());
        return categoryService.addCategory(newCategory);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int catId) {
        log.info("Deleting category id {}", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable int catId,
                                      @RequestBody CategoryDto updatedCategory) {
        log.info("Updating category id {}", catId);
        return categoryService.updateCategory(catId, updatedCategory);
    }

    // Публичные методы

    @GetMapping("/categories")
    public List<CategoryDto> findCategories(@RequestParam(required = false, defaultValue = "0") int from,
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Getting categories with params: from {}, size {}", from, size);
        return categoryService.findCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto findCategoryById(@PathVariable int catId) {
        log.info("Getting category id {}", catId);
        return categoryService.findCategoryById(catId);
    }
}
