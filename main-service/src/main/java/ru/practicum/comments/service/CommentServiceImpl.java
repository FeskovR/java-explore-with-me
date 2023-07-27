package ru.practicum.comments.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comments.model.*;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.error.exception.ValidationException;
import ru.practicum.event.enums.EventStatus;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.model.EventShortDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserMapper;
import ru.practicum.user.model.UserShortDto;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.Validator;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    UserRepository userRepository;
    EventRepository eventRepository;

    //private
    @Override
    public CommentDto addComment(int userId, int eventId, NewCommentDto newCommentDto) {
        Validator.validate(newCommentDto);

        User commenter = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User id " + userId + " not found")
        );

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event id " + eventId + " not found")
        );

        if (event.getState() != EventStatus.PUBLISHED) {
            throw new ValidationException("Event not published");
        }

        Comment comment = CommentMapper.toComment(newCommentDto, commenter, event);
        Comment returned = commentRepository.save(comment);

        return CommentMapper.toCommentDto(returned,
                UserMapper.toUserShortDto(commenter),
                EventMapper.toEventShortDto(event));
    }

    @Override
    public CommentDto updateCommentByCommenter(int userId, int commentId, UpdateCommentByCommenterRequest updateCommentByCommenterRequest) {
        Validator.validate(updateCommentByCommenterRequest);

        User commenter = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User id " + userId + " not found")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment id " + commentId + " not found")
        );

        if (comment.getCommenter().getId() != userId) {
            throw new ValidationException("User id " + userId + " is not owner of comment id " + commentId);
        }

        Event event = comment.getEvent();

        Comment updated = CommentMapper.toComment(updateCommentByCommenterRequest, comment);
        Comment returned = commentRepository.save(updated);

        return CommentMapper.toCommentDto(returned,
                UserMapper.toUserShortDto(commenter),
                EventMapper.toEventShortDto(event));
    }

    @Override
    public void deleteCommentByUser(int userId, int commentId) {
        if (!userRepository.existsById(userId))
            throw new NotFoundException("User id " + userId + " not found");
        if (!commentRepository.existsById(commentId))
            throw new NotFoundException("Comment id " + commentId + " not found");

        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getAllUserComments(int userId) {
        User commenter = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User id " + userId + " not found")
        );
        UserShortDto userShortDto = UserMapper.toUserShortDto(commenter);

        List<Comment> userComments = commentRepository.findAllByCommenterId(userId);
        List<CommentDto> commentDtoList = new ArrayList<>();

        for (Comment userComment : userComments) {
            Event event = userComment.getEvent();
            EventShortDto eventShortDto = EventMapper.toEventShortDto(event);

            commentDtoList.add(CommentMapper.toCommentDto(userComment, userShortDto, eventShortDto));
        }

        return commentDtoList;
    }

    @Override
    public List<CommentDto> getPublishedEventComments(int userId, int eventId) {
        User commenter = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User id " + userId + " not found")
        );

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event id " + eventId + " not found")
        );

        List<Comment> publishedComments = commentRepository.findAllPublishedCommentsByEvent(eventId);
        List<CommentDto> commentDtoList = new ArrayList<>();
        UserShortDto commenterShort = UserMapper.toUserShortDto(commenter);
        EventShortDto eventShort = EventMapper.toEventShortDto(event);

        for (Comment publishedComment : publishedComments) {
            commentDtoList.add(CommentMapper.toCommentDto(publishedComment, commenterShort, eventShort));
        }

        return commentDtoList;
    }

    //Admin
    @Override
    public CommentDto updateCommentByAdmin(int commentId, UpdateCommentByAdminRequest updateCommentByAdminRequest) {
        Validator.validate(updateCommentByAdminRequest);

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment id " + commentId + " not found")
        );

        Comment updated = CommentMapper.toComment(updateCommentByAdminRequest, comment);
        Comment returned = commentRepository.save(updated);

        return CommentMapper.toCommentDto(returned,
                UserMapper.toUserShortDto(comment.getCommenter()),
                EventMapper.toEventShortDto(comment.getEvent()));
    }

    @Override
    public void deleteCommentByAdmin(int commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Comment id " + commentId + " not found");
        }

        commentRepository.deleteById(commentId);
    }
}
