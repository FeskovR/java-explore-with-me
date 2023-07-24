package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.event.enums.UserEventState;

@Data
@AllArgsConstructor
public class UpdateEventUserRequest {
    private String title;
    private String annotation;
    private int category;
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private int participantLimit;
    private Boolean requestModeration;
    private UserEventState stateAction;
}
