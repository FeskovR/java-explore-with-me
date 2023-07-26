package ru.practicum.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.comments.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByCommenterId(int userId);

    @Query("select c from Comment as c " +
            "where c.event.id = :eventId and c.status = 'PUBLISHED'")
    List<Comment> findAllPublishedCommentsByEvent(@Param("eventId") int eventId);
}
