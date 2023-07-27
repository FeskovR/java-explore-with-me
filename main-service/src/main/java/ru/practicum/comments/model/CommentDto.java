package ru.practicum.comments.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.comments.enums.CommentStatus;
import ru.practicum.event.model.EventShortDto;
import ru.practicum.user.model.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    int id;
    String title;
    String text;
    LocalDateTime createdOn;
    UserShortDto commenter;
    EventShortDto event;
    CommentStatus status;
}
