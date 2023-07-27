package ru.practicum.comments.service;

import ru.practicum.comments.model.CommentDto;
import ru.practicum.comments.model.NewCommentDto;
import ru.practicum.comments.model.UpdateCommentByAdminRequest;
import ru.practicum.comments.model.UpdateCommentByCommenterRequest;

import java.util.List;

public interface CommentService {
    CommentDto addComment(int userId, int eventId, NewCommentDto newCommentDto);

    CommentDto updateCommentByCommenter(int userId, int commentId, UpdateCommentByCommenterRequest updateCommentByCommenterRequest);

    void deleteCommentByUser(int userId, int commentId);

    List<CommentDto> getAllUserComments(int userId);

    List<CommentDto> getPublishedEventComments(int userId, int eventId);

    CommentDto updateCommentByAdmin(int commentId, UpdateCommentByAdminRequest updateCommentByAdminRequest);

    void deleteCommentByAdmin(int commentId);
}
