package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.CategoryDto;
import ru.practicum.event.enums.EventStatus;
import ru.practicum.user.model.UserDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String annotation;
    private String description;
    @Column(name = "created")
    private LocalDateTime createdOn;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "published")
    private LocalDateTime publishedOn;
    private Boolean paid;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Column(name = "participant_limit")
    private int participantLimit;
    @Column(name = "confirmed_requests")
    private int confirmedRequests;
    private EventStatus state;
    private double locationLat;
    private double locationLon;
    private CategoryDto category;
    private UserDto initiator;
}
