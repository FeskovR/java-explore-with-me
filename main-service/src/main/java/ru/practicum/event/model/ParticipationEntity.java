package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.enums.EventRequestStatus;
import ru.practicum.user.model.UserDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participations")
public class ParticipationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private EventEntity event;

    private LocalDateTime created;

    @ManyToOne
    private UserDto requester;

    @Enumerated(value = EnumType.STRING)
    private EventRequestStatus status;
}
