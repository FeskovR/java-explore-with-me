package ru.practicum.comments.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.model.CommentDto;
import ru.practicum.comments.model.UpdateCommentByAdminRequest;
import ru.practicum.comments.service.CommentService;

@RestController
@RequestMapping("/admin/comments")
@Slf4j
@AllArgsConstructor
public class AdminCommentController {
    CommentService commentService;

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable int commentId,
                                    @RequestBody UpdateCommentByAdminRequest updateCommentByAdminRequest) {
        log.info("Updating comment id {} by admin", commentId);
        return commentService.updateCommentByAdmin(commentId, updateCommentByAdminRequest);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable int commentId) {
        log.info("Deleting comment id {} by admin", commentId);
        commentService.deleteCommentByAdmin(commentId);
    }
}
