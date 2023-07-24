package ru.practicum.request.service;

import ru.practicum.event.model.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getUserRequests(int userId);

    ParticipationRequestDto addNewRequest(int userId, int eventId);

    ParticipationRequestDto cancelUserRequest(int userId, int requestId);
}
