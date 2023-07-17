package ru.practicum.event.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.StatClient;
import ru.practicum.category.model.CategoryDto;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.event.enums.EventSort;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.model.StatDto;
import ru.practicum.user.model.UserDto;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    EventRepository eventRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;
    StatClient statClient;

    @Override
    public List<EventShortDto> findAllUserEventsByUser(int userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<EventEntity> allUserEvents = eventRepository.findByInitiatorId(userId, pageable);

    }

//    @Override
//    public EventFullDto addNewEventByUser(int userId, NewEventDto event) {
//        UserDto user = userRepository.findById(userId).orElseThrow(
//                () -> new NotFoundException("User id: " + userId + " not found")
//        );
//
//        CategoryDto categoryDto = categoryRepository.findById(event.getCategory()).orElseThrow(
//                () -> new NotFoundException("Category id: " + event.getCategory() + " not found")
//        );
//
//        EventFullDto eventFullDto = EventMapper.toEventFullDto(user, event, categoryDto, 0);
//
//        LocationEntity locationEntity = locationRepository.save(eventFullDto.getLocation());
//
//        eventFullDto.getLocation().setId(locationEntity.getId());
//
//        //todo refactor
//        EventFullDto eventFromDB = eventRepository.save(eventFullDto);
//
//        System.out.println(eventFromDB);
//
//        return eventFromDB;
//    }
//
//    @Override
//    public EventFullDto getEventByIdByUser(int userId, int eventId) {
//        EventFullDto eventFullDto = eventRepository.findById(eventId).orElseThrow(
//                () -> new NotFoundException("Event id: " + eventId + " not found")
//        );
//
//        userRepository.findById(userId).orElseThrow(
//                () -> new NotFoundException("User id: " + userId + " not found")
//        );
//
//        if (eventFullDto.getInitiator().getId() != userId) {
//            throw new NotFoundException("Event id: " + eventId + " not available");
//        }
//
//        return eventFullDto;
//    }
//
//    @Override
//    public EventFullDto updateEventByUser(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest) {
//        return null;
//    }
//
//    @Override
//    public EventFullDto updateEvent(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest) {
//        UserDto userDto = userRepository.findById(userId).orElseThrow(
//                () -> new NotFoundException("User id: " + userId + " not found")
//        );
//
//        EventFullDto eventFullDto = eventRepository.findById(eventId).orElseThrow(
//                () -> new NotFoundException("Event id: " + eventId + " not found")
//        );
//
//        CategoryDto categoryDto = null;
//        if (updateEventUserRequest.getCategory() != 0) {
//            categoryDto = categoryRepository.findById(updateEventUserRequest.getCategory()).orElseThrow(
//                    () -> new NotFoundException("Category id: " + updateEventUserRequest.getCategory() + " not found")
//            );
//        }
//
//        EventFullDto updatedEvent = EventMapper.toEventFullDto(updateEventUserRequest, eventFullDto, categoryDto);
//
//        return eventRepository.save(updatedEvent);
//    }
//
//    @Override
//    public List<ParticipationRequestDto> findEventParticipationRequestsByUser(int userId, int eventId) {
//        return null;
//    }
//
//    @Override
//    public EventRequestStatusUpdateResult updateRequestStatusByUser(int userId, int eventId, EventRequestStatusUpdateRequest body) {
//        return null;
//    }
//
//    @Override
//    public List<EventShortDto> findEventsByAdmin(List<Integer> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
//        return null;
//    }
//
//    @Override
//    public EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest body) {
//        return null;
//    }
//
//    @Override
//    public List<EventShortDto> findEventsPublic(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable, EventSort sort, int from, int size) {
//        return null;
//    }
//
//    @Override
//    public EventFullDto findEventByIdPublic(int id) {
//        return null;
//    }
//
//    @Override
//    public EventRequestStatusUpdateResult updateRequestStatus(int userId, int eventId, EventRequestStatusUpdateRequest body) {
//        return null;
//    }

    private List<StatDto> getStat(List<String> uris, LocalDateTime start, LocalDateTime end, boolean unique ) {
        ResponseEntity<Object> response = statClient.getStat(start, end, uris, unique);
        return (List<StatDto>) response.getBody();
    }
}
