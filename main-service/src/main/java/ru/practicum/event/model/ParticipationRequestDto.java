package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.event.enums.EventRequestStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NonNull
public class ParticipationRequestDto {
    private int id;
    private int event;
    private LocalDateTime created;
    private int requester;
    private EventRequestStatus status;
}
