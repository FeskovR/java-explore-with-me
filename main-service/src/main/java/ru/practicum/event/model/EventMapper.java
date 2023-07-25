package ru.practicum.event.model;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.Category;
import ru.practicum.event.enums.AdminEventState;
import ru.practicum.event.enums.EventStatus;
import ru.practicum.event.enums.UserEventState;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserMapper;
import ru.practicum.user.model.UserShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class EventMapper {

    public Event toEventEntity(NewEventDto newEventDto,
                               Category category,
                               User initiator) {
        return new Event(0,
                newEventDto.getTitle(),
                newEventDto.getAnnotation(),
                newEventDto.getDescription(),
                LocalDateTime.now(),
                newEventDto.getEventDate(),
                null,
                newEventDto.getPaid(),
                newEventDto.getRequestModeration(),
                newEventDto.getParticipantLimit(),
                0,
                EventStatus.PENDING,
                newEventDto.getLocation().getLat(),
                newEventDto.getLocation().getLon(),
                category,
                initiator);
    }

    public Event toEventEntity(UpdateEventUserRequest update,
                               Event eventToUpdate,
                               Category category) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (update.getTitle() != null)
            eventToUpdate.setTitle(update.getTitle());
        if (update.getAnnotation() != null)
            eventToUpdate.setAnnotation(update.getAnnotation());
        eventToUpdate.setCategory(category);
        if (update.getDescription() != null)
            eventToUpdate.setDescription(update.getDescription());
        if (update.getEventDate() != null)
            eventToUpdate.setEventDate(LocalDateTime.parse(update.getEventDate(), formatter));
        if (update.getLocation() != null) {
            eventToUpdate.setLocationLat(update.getLocation().getLat());
            eventToUpdate.setLocationLon(update.getLocation().getLon());
        }
        if (update.getPaid() != null)
            eventToUpdate.setPaid(update.getPaid());
        if (update.getParticipantLimit() != 0)
            eventToUpdate.setParticipantLimit(update.getParticipantLimit());
        if (update.getRequestModeration() != null)
            eventToUpdate.setRequestModeration(update.getRequestModeration());
        if (update.getStateAction() == UserEventState.SEND_TO_REVIEW) {
            eventToUpdate.setState(EventStatus.PENDING);
        } else if (update.getStateAction() == UserEventState.CANCEL_REVIEW) {
            eventToUpdate.setState(EventStatus.CANCELED);
        }

        return eventToUpdate;
    }

    public Event toEventEntity(UpdateEventAdminRequest update,
                               Event eventToUpdate,
                               Category category) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (update.getTitle() != null)
            eventToUpdate.setTitle(update.getTitle());
        if (update.getAnnotation() != null)
            eventToUpdate.setAnnotation(update.getAnnotation());
        eventToUpdate.setCategory(category);
        if (update.getDescription() != null)
            eventToUpdate.setDescription(update.getDescription());
        if (update.getEventDate() != null)
            eventToUpdate.setEventDate(LocalDateTime.parse(update.getEventDate(), formatter));
        if (update.getLocation() != null) {
            eventToUpdate.setLocationLat(update.getLocation().getLat());
            eventToUpdate.setLocationLon(update.getLocation().getLon());
        }
        if (update.getPaid() != null)
            eventToUpdate.setPaid(update.getPaid());
        if (update.getParticipantLimit() != 0)
            eventToUpdate.setParticipantLimit(update.getParticipantLimit());
        if (update.getRequestModeration() != null)
            eventToUpdate.setRequestModeration(update.getRequestModeration());
        if (update.getStateAction() == AdminEventState.REJECT_EVENT) {
            eventToUpdate.setState(EventStatus.CANCELED);
        } else if (update.getStateAction() == AdminEventState.PUBLISH_EVENT) {
            eventToUpdate.setState(EventStatus.PUBLISHED);
            eventToUpdate.setPublishedOn(LocalDateTime.now());
        }

        return eventToUpdate;
    }

    public Event toEventEntity(EventFullDto eventFullDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return new Event(eventFullDto.getId(),
                eventFullDto.getTitle(),
                eventFullDto.getAnnotation(),
                eventFullDto.getDescription(),
                LocalDateTime.parse(eventFullDto.getCreatedOn(), formatter),
                LocalDateTime.parse(eventFullDto.getEventDate(), formatter),
                eventFullDto.getPublishedOn() == null ? null : LocalDateTime.parse(eventFullDto.getPublishedOn(), formatter),
                eventFullDto.getPaid(),
                eventFullDto.getRequestModeration(),
                eventFullDto.getParticipantLimit(),
                eventFullDto.getConfirmedRequests(),
                eventFullDto.getState(),
                eventFullDto.getLocation().getLat(),
                eventFullDto.getLocation().getLon(),
                eventFullDto.getCategory(),
                new User());
    }

    public EventFullDto toEventFullDto(Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createdOn = event.getCreatedOn().format(formatter);
        String eventDate = event.getEventDate().format(formatter);
        String publishedOn = null;
        if (event.getPublishedOn() != null) {
            publishedOn = event.getPublishedOn().format(formatter);
        }
        Location location = new Location(
                event.getLocationLat(),
                event.getLocationLon()
        );

        return new EventFullDto(event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getDescription(),
                createdOn,
                eventDate,
                publishedOn,
                event.getPaid(),
                event.getRequestModeration(),
                event.getParticipantLimit(),
                event.getConfirmedRequests(),
                event.getState(),
                event.getCategory(),
                UserMapper.toUserShortDto(event.getInitiator()),
                location,
                0);
    }

    public EventShortDto toEventShortDto(Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String eventDate = event.getEventDate().format(formatter);
        UserShortDto initiator = UserMapper.toUserShortDto(event.getInitiator());

        return new EventShortDto(event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getConfirmedRequests(),
                eventDate,
                event.getPaid(),
                0,
                event.getCategory(),
                initiator);
    }
}
