package ru.practicum.comments.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.comments.enums.CommentStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentByAdminRequest {
    String title;
    String text;
    CommentStatus status;
}
