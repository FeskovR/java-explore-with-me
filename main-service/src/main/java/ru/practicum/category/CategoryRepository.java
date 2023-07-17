package ru.practicum.category;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.model.CategoryDto;

public interface CategoryRepository extends JpaRepository<CategoryDto, Integer> {
}
