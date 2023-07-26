package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.UserShortDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private int id;
    private String title;
    private String annotation;
    private int confirmedRequests;
    private String eventDate;
    private Boolean paid;
    private long views;
    private Category category;
    private UserShortDto initiator;
}
