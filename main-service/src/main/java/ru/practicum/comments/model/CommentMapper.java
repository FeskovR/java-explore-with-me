package ru.practicum.comments.model;

import lombok.experimental.UtilityClass;
import ru.practicum.comments.enums.CommentStatus;
import ru.practicum.error.exception.ValidationException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventShortDto;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserShortDto;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public Comment toComment(NewCommentDto newCommentDto, User commenter, Event event) {
        return new Comment(0,
                newCommentDto.getTitle(),
                newCommentDto.getText(),
                LocalDateTime.now(),
                commenter,
                event,
                CommentStatus.WAITING);
    }

    public Comment toComment(UpdateCommentByCommenterRequest updateCommentByCommenterRequest,
                             Comment comment) {
        if (updateCommentByCommenterRequest.getTitle() != null) {
            comment.setTitle(updateCommentByCommenterRequest.getTitle());
        }
        if (updateCommentByCommenterRequest.getText() != null) {
            comment.setText(updateCommentByCommenterRequest.getText());
        }

        return comment;
    }

    public Comment toComment(UpdateCommentByAdminRequest updateCommentByAdminRequest,
                             Comment comment) {
        if (updateCommentByAdminRequest.getTitle() != null) {
            comment.setTitle(updateCommentByAdminRequest.getTitle());
        }
        if (updateCommentByAdminRequest.getText() != null) {
            comment.setText(updateCommentByAdminRequest.getText());
        }
        if (updateCommentByAdminRequest.getStatus() != null) {
            if (updateCommentByAdminRequest.getStatus().equals(CommentStatus.PUBLISHED)) {
                comment.setStatus(CommentStatus.PUBLISHED);
            } else if (updateCommentByAdminRequest.getStatus().equals(CommentStatus.WAITING)) {
                comment.setStatus(CommentStatus.WAITING);
            } else {
                throw new ValidationException("Unknown comment status");
            }
        }

        return comment;
    }


    public CommentDto toCommentDto(Comment comment,
                                   UserShortDto userShortDto,
                                   EventShortDto eventShortDto) {
        return new CommentDto(comment.getId(),
                comment.getTitle(),
                comment.getText(),
                comment.getCreatedOn(),
                userShortDto,
                eventShortDto,
                comment.getStatus());
    }
}
