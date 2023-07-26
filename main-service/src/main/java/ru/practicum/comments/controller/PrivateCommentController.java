package ru.practicum.comments.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.model.CommentDto;
import ru.practicum.comments.model.NewCommentDto;
import ru.practicum.comments.model.UpdateCommentByCommenterRequest;
import ru.practicum.comments.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@Slf4j
@AllArgsConstructor
public class PrivateCommentController {
    CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable int userId,
                                 @RequestParam int eventId,
                                 @RequestBody NewCommentDto newCommentDto) {
        log.info("Adding new comment from user id {} to event id {}", userId, eventId);
        return commentService.addComment(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@RequestBody UpdateCommentByCommenterRequest updateCommentByCommenterRequest,
                                    @PathVariable int commentId,
                                    @PathVariable int userId) {
        log.info("Updating comment id {} by user id {}", commentId, userId);
        return commentService.updateCommentByCommenter(userId, commentId, updateCommentByCommenterRequest);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByUser(@PathVariable int commentId,
                                    @PathVariable int userId) {
        log.info("Deleting comment id {} by user id {}", commentId, userId);
        commentService.deleteCommentByUser(userId, commentId);
    }

    @GetMapping
    public List<CommentDto> getAllUserComments(@PathVariable int userId) {
        log.info("Getting all user id {} comments", userId);
        return commentService.getAllUserComments(userId);
    }

    @GetMapping("/event")
    public List<CommentDto> getPublishedEventComments(@RequestParam int eventId,
                                                      @PathVariable int userId) {
        log.info("Getting all event id {} comments by user id {}", eventId, userId);
        return commentService.getPublishedEventComments(userId, eventId);
    }
}
