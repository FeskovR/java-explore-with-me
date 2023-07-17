package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.CategoryDto;
import ru.practicum.event.enums.EventStatus;
import ru.practicum.user.model.UserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private int id;
    private String title;
    private String annotation;
    private String description;
    private String createdOn;
    private String eventDate;
    private String publishedOn;
    private Boolean paid;
    private Boolean requestModeration;
    private int participantLimit;
    private int confirmedRequests;
    private EventStatus state;
    private CategoryDto category;
    private UserDto initiator;
    private Location location;
    private int views;
}
