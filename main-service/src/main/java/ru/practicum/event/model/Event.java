package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.Category;
import ru.practicum.event.enums.EventStatus;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
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
    @Enumerated(value = EnumType.STRING)
    private EventStatus state;
    @Column(name = "location_lat")
    private double locationLat;
    @Column(name = "location_lon")
    private double locationLon;
    @ManyToOne
    private Category category;
    @ManyToOne
    private User initiator;
}
