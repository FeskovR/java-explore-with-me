package ru.practicum.category.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.category.model.CategoryDto;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.category.model.NewCategoryDto;
import ru.practicum.error.exception.BreakingRulesException;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.event.model.EventEntity;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.util.Validator;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    EventRepository eventRepository;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Validator.validate(newCategoryDto);
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(newCategoryDto);
        return categoryRepository.save(categoryDto);
    }

    @Override
    public CategoryDto updateCategory(int catId, CategoryDto categoryDto) {
        CategoryDto updatedCategoryDto = categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category id " + catId + " not found")
        );

        Validator.validate(categoryDto);
        updatedCategoryDto.setName(categoryDto.getName());

        return categoryRepository.save(updatedCategoryDto);
    }

    @Override
    public void deleteCategory(int catId) {
        List<EventEntity> eventEntityList = eventRepository.findAllEventsByCategoryId(catId);
        if (eventEntityList.size() > 0) {
            throw new BreakingRulesException("Cannot delete category with events");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> findCategories(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageable).toList();
    }

    @Override
    public CategoryDto findCategoryById(int catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category id " + catId + "not found")
        );
    }
}
