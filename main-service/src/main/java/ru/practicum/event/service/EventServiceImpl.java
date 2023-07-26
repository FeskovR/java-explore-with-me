package ru.practicum.event.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.StatClient;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.error.exception.BreakingRulesException;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.error.exception.ValidationException;
import ru.practicum.event.comparator.CompareEventByEventDate;
import ru.practicum.event.comparator.CompareEventByViews;
import ru.practicum.event.enums.AdminEventState;
import ru.practicum.event.enums.EventRequestStatus;
import ru.practicum.event.enums.EventSort;
import ru.practicum.event.enums.EventStatus;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.model.HitDto;
import ru.practicum.request.model.ParticipationMapper;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.Validator;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    EventRepository eventRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;
    StatClient statClient;
    RequestRepository requestRepository;
    StatisticService statisticService;

    //private
    @Override
    public List<EventShortDto> findAllUserEventsByUser(int userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> allUserEvents = eventRepository.findByInitiatorId(userId, pageable);
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        for (Event event : allUserEvents) {
            eventShortDtoList.add(EventMapper.toEventShortDto(event));
        }

        eventShortDtoList = statisticService.setViewsToEventShortDto(eventShortDtoList);
        return eventShortDtoList;
    }

    @Override
    public EventFullDto addNewEventByUser(int userId, NewEventDto event) {
        Validator.validate(event);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User id: " + userId + " not found")
        );

        Category category = categoryRepository.findById(event.getCategory()).orElseThrow(
                () -> new NotFoundException("Category id: " + event.getCategory() + " not found")
        );

        Event eventEntity = EventMapper.toEventEntity(event, category, user);
        Event returned = eventRepository.save(eventEntity);

        return EventMapper.toEventFullDto(returned);
    }

    @Override
    public EventFullDto getEventByIdByUser(int userId, int eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User id: " + userId + " not found");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event id: " + eventId + " not found")
        );

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto = statisticService.setViewsToEventFullDto(eventFullDto);

        return eventFullDto;
    }

    @Override
    public EventFullDto updateEventByUser(int userId, int eventId, UpdateEventUserRequest body) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User id: " + userId + " not found");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event id: " + eventId + " not found")
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Category category;
        if (body.getCategory() != 0) {
            category = categoryRepository.findById(body.getCategory()).orElseThrow(
                    () -> new NotFoundException("Category id: " + body.getCategory() + " not found")
            );
        } else {
            category = event.getCategory();
        }

        if (event.getState() == EventStatus.PUBLISHED) {
            throw new BreakingRulesException("Event must not be published");
        }

        if ((body.getEventDate() != null &&
            LocalDateTime.parse(body.getEventDate(), formatter).isBefore(LocalDateTime.now().plusHours(2))) ||
            event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Event date cannot be earlier than 2 hours");
        }

        Event updatedEvent = EventMapper.toEventEntity(body, event, category);
        Validator.validate(updatedEvent);

        Event returned = eventRepository.save(updatedEvent);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(returned);
        eventFullDto = statisticService.setViewsToEventFullDto(eventFullDto);

        return eventFullDto;
    }

    @Override
    public List<ParticipationRequestDto> findEventParticipationRequestsByUser(int userId, int eventId) {
        Event event = checkUserIsExistAndInitiatorOfEventAndGetEvent(userId, eventId);

        List<Participation> participationList = requestRepository.findEventOwnerRequests(eventId);
        List<ParticipationRequestDto> result = new ArrayList<>();

        for (Participation entity : participationList) {
            result.add(ParticipationMapper.toParticipationRequestDto(entity));
        }

        return result;
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatusByUser(int userId, int eventId, EventRequestStatusUpdateRequest body) {
        Event event = checkUserIsExistAndInitiatorOfEventAndGetEvent(userId, eventId);

        List<Participation> eventRequests = requestRepository.findRequestsByIds(body.getRequestIds());

        for (Participation eventRequest : eventRequests) {
            if (body.getRequestIds().contains(eventRequest.getId())) {
                if (body.getStatus() == EventRequestStatus.CONFIRMED &&
                    (event.getConfirmedRequests() < event.getParticipantLimit() ||
                    event.getParticipantLimit() == 0)) {
                        eventRequest.setStatus(EventRequestStatus.CONFIRMED);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                } else if (body.getStatus() == EventRequestStatus.CONFIRMED &&
                            event.getConfirmedRequests() >= event.getParticipantLimit() &&
                            event.getParticipantLimit() != 0) {
                                throw new BreakingRulesException("Limit of participant");
                } else if (body.getStatus() == EventRequestStatus.REJECTED) {
                    Optional<Participation> participation = requestRepository.findById(eventRequest.getId());
                    if (participation.isPresent() &&
                        participation.get().getStatus() == EventRequestStatus.CONFIRMED) {
                        throw new BreakingRulesException("Cannot reject confirmed request");
                    }
                    eventRequest.setStatus(EventRequestStatus.REJECTED);
                }
            }

            requestRepository.save(eventRequest);
        }

        eventRepository.save(event);

        List<Participation> eventRequestsUpdatedAll = requestRepository.findAllByEventId(eventId);
        List<Participation> confirmedRequests = new ArrayList<>();
        List<Participation> rejectedRequests = new ArrayList<>();

        for (Participation entity : eventRequestsUpdatedAll) {
            if (entity.getStatus() == EventRequestStatus.CONFIRMED) {
                confirmedRequests.add(entity);
            } else if (entity.getStatus() == EventRequestStatus.REJECTED) {
                rejectedRequests.add(entity);
            }
        }

        List<ParticipationRequestDto> confirmedParticipations = new ArrayList<>();
        List<ParticipationRequestDto> rejectedParticipations = new ArrayList<>();

        for (Participation confirmedRequest : confirmedRequests) {
            confirmedParticipations.add(ParticipationMapper.toParticipationRequestDto(confirmedRequest));
        }
        for (Participation rejectedRequest : rejectedRequests) {
            rejectedParticipations.add(ParticipationMapper.toParticipationRequestDto(rejectedRequest));
        }

        return new EventRequestStatusUpdateResult(confirmedParticipations, rejectedParticipations);
    }

    //Admin
    @Override
    public List<EventFullDto> findEventsByAdmin(List<Integer> users,
                                                 List<String> states,
                                                 List<Integer> categories,
                                                 LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd,
                                                 int from,
                                                 int size) {
        List<Event> preResult = eventRepository.findAll();
        List<Event> result = new ArrayList<>();

        if (users != null && !(users.size() == 1 && users.get(0) == 0)) {
            for (Event event : preResult) {
                if (users.contains(event.getInitiator().getId())) {
                    result.add(event);
                }
            }
            preResult = result;
            result = new ArrayList<>();
        }

        if (states != null && !(states.size() == 1 && states.get(0) == null)) {
            for (Event event : preResult) {
                if (states.contains(event.getState().toString())) {
                    result.add(event);
                }
            }
            preResult = result;
            result = new ArrayList<>();
        }

        if (categories != null && !(categories.size() == 1 && categories.get(0) == 0)) {
            for (Event event : preResult) {
                if (categories.contains(event.getCategory().getId())) {
                    result.add(event);
                }
            }
            preResult = result;
            result = new ArrayList<>();
        }

        if (rangeStart != null) {
            for (Event event : preResult) {
                if (event.getEventDate().isAfter(rangeStart)) {
                    result.add(event);
                }
                preResult = result;
                result = new ArrayList<>();
            }
        }

        if (rangeEnd != null) {
            for (Event event : preResult) {
                if (event.getEventDate().isBefore(rangeEnd)) {
                    result.add(event);
                }
            }
            preResult = result;
        }

        List<EventFullDto> resultList = new ArrayList<>();
        for (Event event : preResult) {
            resultList.add(EventMapper.toEventFullDto(event));
        }
        resultList = resultList.subList(from, Math.min(resultList.size(), from + size));
        if (resultList.size() > 0) {
            resultList = statisticService.setViewsToEventFullDto(resultList);
        }

        return resultList;
    }

    @Override
    public EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest body) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (body.getEventDate() != null &&
            LocalDateTime.parse(body.getEventDate(), formatter).isBefore(LocalDateTime.now())) {
            throw new ValidationException("Event date cannot be early than now");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event id: " + eventId + " not found")
        );

        Category category;
        if (body.getCategory() != 0) {
            category = categoryRepository.findById(body.getCategory()).orElseThrow(
                    () -> new NotFoundException("Category id: " + body.getCategory() + " not found")
            );
        } else {
            category = event.getCategory();
        }

        if (body.getStateAction() == AdminEventState.PUBLISH_EVENT &&
            event.getState() != EventStatus.PENDING) {
            throw new BreakingRulesException("Updating event must be in \"PENDING\" status");
        }

        if (body.getStateAction() == AdminEventState.REJECT_EVENT &&
            event.getState() == EventStatus.PUBLISHED) {
            throw new BreakingRulesException("Can't reject a published event");
        }

        Event updatedEvent = EventMapper.toEventEntity(body, event, category);
        Validator.validate(updatedEvent);

        if (updatedEvent.getPublishedOn() != null &&
            updatedEvent.getPublishedOn().isAfter(updatedEvent.getEventDate().minusHours(1))) {
            throw new BreakingRulesException("Event date cannot be early than 1 hour");
        }

        Event returned = eventRepository.save(updatedEvent);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(returned);
        eventFullDto = statisticService.setViewsToEventFullDto(eventFullDto);

        return eventFullDto;
    }

    //Public
    @Override
    public List<EventShortDto> findEventsPublic(String text,
                                                List<Integer> categories,
                                                Boolean paid,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                Boolean onlyAvailable,
                                                EventSort sort,
                                                int from,
                                                int size,
                                                HttpServletRequest request) {
        if (rangeEnd != null && rangeStart != null && rangeEnd.isBefore(rangeStart)) {
            throw new ValidationException("Range and cannot be before range start");
        }

        List<Event> preResult = eventRepository.findAllPublished();
        List<Event> result = new ArrayList<>();

        if (text != null) {
            for (Event event : preResult) {
                if (event.getAnnotation().toLowerCase().contains(text.toLowerCase()) ||
                event.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    result.add(event);
                }
            }
            preResult = result;
            result = new ArrayList<>();
        }

        if (categories != null && categories.size() > 0) {
            for (Event event : preResult) {
                if (categories.contains(event.getCategory().getId())) {
                    result.add(event);
                }
            }
            preResult = result;
            result = new ArrayList<>();
        }

        if (paid != null) {
            for (Event event : preResult) {
                if (event.getPaid() == paid) {
                    result.add(event);
                }
            }
            preResult = result;
            result = new ArrayList<>();
        }

        if (rangeStart != null) {
            for (Event event : preResult) {
                if (event.getEventDate().isAfter(rangeStart)) {
                    result.add(event);
                }
            }
            preResult = result;
            result = new ArrayList<>();
        }

        if (rangeEnd != null) {
            for (Event event : preResult) {
                if (event.getEventDate().isBefore(rangeEnd)) {
                    result.add(event);
                }
            }
            preResult = result;
            result = new ArrayList<>();
        }

        if (rangeStart == null && rangeEnd == null) {
            for (Event event : preResult) {
                if (event.getEventDate().isAfter(LocalDateTime.now())) {
                    result.add(event);
                }
            }
            preResult = result;
            result = new ArrayList<>();
        }

        if (onlyAvailable != null) {
            for (Event event : preResult) {
                if (onlyAvailable && event.getConfirmedRequests() < event.getParticipantLimit()) {
                    result.add(event);
                } else if (!onlyAvailable) {
                    result.add(event);
                }
            }
            preResult = result;
        }

        if (sort == EventSort.EVENT_DATE) {
            preResult.sort(new CompareEventByEventDate());
        }

        List<EventShortDto> eventShortDtoList = new ArrayList<>();

        for (Event event : preResult) {
            eventShortDtoList.add(EventMapper.toEventShortDto(event));
        }

        if (sort == EventSort.VIEWS) {
            eventShortDtoList.sort(new CompareEventByViews());
        }

        eventShortDtoList = eventShortDtoList.subList(from, Math.min(eventShortDtoList.size(), from + size));
        if (eventShortDtoList.size() > 0) {
            eventShortDtoList = statisticService.setViewsToEventShortDto(eventShortDtoList);
        }

        HitDto hitDto = new HitDto("explore with me",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now());

        statClient.hit(hitDto);

        return eventShortDtoList;
    }

    @Override
    public EventFullDto findEventByIdPublic(int id, HttpServletRequest request) {
        Event event = eventRepository.getEventByIdPublic(id, EventStatus.PUBLISHED).orElseThrow(
                () -> new NotFoundException("Event id: " + id + " not found")
        );

        if (event.getState() != EventStatus.PUBLISHED) {
            throw new NotFoundException("Event id: " + id + " not found");
        }

        HitDto hitDto = new HitDto("explore with me",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now());

        statClient.hit(hitDto);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto = statisticService.setViewsToEventFullDto(eventFullDto);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
        return eventFullDto;
    }

    private Event checkUserIsExistAndInitiatorOfEventAndGetEvent(int userId, int eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User id: " + userId + " not found");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event id " + eventId + " not found")
        );

        if (event.getInitiator().getId() != userId) {
            throw new ValidationException("User id " + userId + "is not initiator of event id " + eventId);
        }

        return event;
    }
}
