package ru.practicum.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.event.enums.UserEventState;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UpdateEventUserRequest {
    private String title;
    private String annotation;
    private int category;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private UserEventState stateAction;
}
