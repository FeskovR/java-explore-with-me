package ru.practicum.request.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@AllArgsConstructor
@Slf4j
public class RequestController {
    RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getUserRequests(@PathVariable int userId) {
        log.info("Getting all user requests to participation in event");
        return requestService.getUserRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addNewRequest(@PathVariable int userId,
                                                 @RequestParam int eventId) {
        log.info("Add new request from user id {} to participation in event id {}", userId, eventId);
        return requestService.addNewRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelUserRequest(@PathVariable int userId,
                                                     @PathVariable int requestId) {
        log.info("Cancel user request");
        return requestService.cancelUserRequest(userId, requestId);
    }
}
