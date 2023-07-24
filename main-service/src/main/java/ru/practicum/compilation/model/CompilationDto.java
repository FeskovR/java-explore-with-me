package ru.practicum.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.EventFullDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private int id;
    private Boolean pinned;
    private String title;
    private List<EventFullDto> events;
}
