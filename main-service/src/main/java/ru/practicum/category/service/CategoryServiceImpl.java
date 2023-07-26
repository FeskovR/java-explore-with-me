package ru.practicum.category.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.category.model.NewCategoryDto;
import ru.practicum.error.exception.BreakingRulesException;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.util.Validator;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    EventRepository eventRepository;

    @Override
    public Category addCategory(NewCategoryDto newCategoryDto) {
        Validator.validate(newCategoryDto);
        Category category = CategoryMapper.toCategoryDto(newCategoryDto);
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(int catId, Category category) {
        Category updatedCategory = categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category id " + catId + " not found")
        );

        Validator.validate(category);
        updatedCategory.setName(category.getName());

        return categoryRepository.save(updatedCategory);
    }

    @Override
    public void deleteCategory(int catId) {
        List<Event> eventList = eventRepository.findAllEventsByCategoryId(catId);
        if (eventList.size() > 0) {
            throw new BreakingRulesException("Cannot delete category with events");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<Category> findCategories(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageable).toList();
    }

    @Override
    public Category findCategoryById(int catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category id " + catId + "not found")
        );
    }
}
