package ru.practicum.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NewCompilationDto {
    private String title;
    private boolean pinned = false;
    private List<Integer> events;
}
