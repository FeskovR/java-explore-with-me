package ru.practicum.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.*;
import ru.practicum.event.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Slf4j
@AllArgsConstructor
public class PrivateEventsController {
    EventService eventService;

    @GetMapping
    public List<EventShortDto> findAllUserEvents(@PathVariable int userId,
                                                 @RequestParam int from,
                                                 @RequestParam int size) {
        log.info("Getting all events by user id: {}", userId);
        return eventService.findAllUserEventsByUser(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addNewEvent(@PathVariable int userId,
                                    @RequestBody NewEventDto event) {
        log.info("Adding new event: \"{}\"", event.getTitle());
        return eventService.addNewEventByUser(userId, event);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable int userId,
                                     @PathVariable int eventId) {
        log.info("Getting user id {} event by id: {}", userId, eventId);
        return eventService.getEventByIdByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable int userId,
                                     @PathVariable int eventId,
                                     @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Updating event by user {}", userId);
        return eventService.updateEventByUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findEventParticipationRequests(@PathVariable int userId,
                                                                        @PathVariable int eventId) {
        log.info("Getting participation's requests for event id {}", eventId);
        return eventService.findEventParticipationRequestsByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestStatus(@PathVariable int userId,
                                                              @PathVariable int eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest body) {
        log.info("Updating requests for participation's status for event id {}", eventId);
        return eventService.updateRequestStatusByUser(userId, eventId, body);
    }
}
