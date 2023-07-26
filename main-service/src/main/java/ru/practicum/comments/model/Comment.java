package ru.practicum.comments.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.comments.enums.CommentStatus;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    String text;
    @Column(name = "created_on")
    LocalDateTime createdOn;
    @ManyToOne
    User commenter;
    @ManyToOne
    Event event;
    @Enumerated(value = EnumType.STRING)
    CommentStatus status;
}
