package ru.practicum.event.service;


import ru.practicum.event.enums.EventSort;
import ru.practicum.event.model.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    // private
    List<EventShortDto> findAllUserEventsByUser(int userId,
                                                int from,
                                                int size);

    EventFullDto addNewEventByUser(int userId,
                                   NewEventDto event);

    EventFullDto getEventByIdByUser(int userId,
                                    int eventId);

    EventFullDto updateEventByUser(int userId,
                                   int eventId,
                                   UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> findEventParticipationRequestsByUser(int userId,
                                                                       int eventId);

    EventRequestStatusUpdateResult updateRequestStatusByUser(int userId,
                                                             int eventId,
                                                             EventRequestStatusUpdateRequest body);

    // admin
    List<EventShortDto> findEventsByAdmin(List<Integer> users,
                                   List<String> states,
                                   List<Integer> categories,
                                   LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd,
                                   int from,
                                   int size);

    EventFullDto updateEventByAdmin(int eventId,
                             UpdateEventAdminRequest body);

    // public
    List<EventShortDto> findEventsPublic(String text,
                                   List<Integer> categories,
                                   boolean paid,
                                   LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd,
                                   boolean onlyAvailable,
                                   EventSort sort,
                                   int from,
                                   int size);

    EventFullDto findEventByIdPublic(int id);
}
