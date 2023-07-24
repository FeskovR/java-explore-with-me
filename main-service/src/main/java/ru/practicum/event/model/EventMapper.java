package ru.practicum.event.model;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.CategoryDto;
import ru.practicum.event.enums.AdminEventState;
import ru.practicum.event.enums.EventStatus;
import ru.practicum.event.enums.UserEventState;
import ru.practicum.user.model.UserDto;
import ru.practicum.user.model.UserMapper;
import ru.practicum.user.model.UserShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class EventMapper {

    public EventEntity toEventEntity(NewEventDto newEventDto,
                                     CategoryDto category,
                                     UserDto initiator) {
        return new EventEntity(0,
                newEventDto.getTitle(),
                newEventDto.getAnnotation(),
                newEventDto.getDescription(),
                LocalDateTime.now(),
                newEventDto.getEventDate(),
                null,
                newEventDto.isPaid(),
                newEventDto.isRequestModeration(),
                newEventDto.getParticipantLimit(),
                0,
                EventStatus.PENDING,
                newEventDto.getLocation().getLat(),
                newEventDto.getLocation().getLon(),
                category,
                initiator);
    }

    public EventEntity toEventEntity(UpdateEventUserRequest update,
                                     EventEntity eventToUpdate,
                                     CategoryDto category) {
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

    public EventEntity toEventEntity(UpdateEventAdminRequest update,
                                     EventEntity eventToUpdate,
                                     CategoryDto category) {
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

    public EventEntity toEventEntity(EventFullDto eventFullDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return new EventEntity(eventFullDto.getId(),
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
                new UserDto());
    }

    public EventFullDto toEventFullDto(EventEntity eventEntity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createdOn = eventEntity.getCreatedOn().format(formatter);
        String eventDate = eventEntity.getEventDate().format(formatter);
        String publishedOn = null;
        if (eventEntity.getPublishedOn() != null) {
            publishedOn = eventEntity.getPublishedOn().format(formatter);
        }
        Location location = new Location(
                eventEntity.getLocationLat(),
                eventEntity.getLocationLon()
        );

        return new EventFullDto(eventEntity.getId(),
                eventEntity.getTitle(),
                eventEntity.getAnnotation(),
                eventEntity.getDescription(),
                createdOn,
                eventDate,
                publishedOn,
                eventEntity.getPaid(),
                eventEntity.getRequestModeration(),
                eventEntity.getParticipantLimit(),
                eventEntity.getConfirmedRequests(),
                eventEntity.getState(),
                eventEntity.getCategory(),
                UserMapper.toUserShortDto(eventEntity.getInitiator()),
                location,
                0);
    }

    public EventShortDto toEventShortDto(EventEntity eventEntity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String eventDate = eventEntity.getEventDate().format(formatter);
        UserShortDto initiator = UserMapper.toUserShortDto(eventEntity.getInitiator());

        return new EventShortDto(eventEntity.getId(),
                eventEntity.getTitle(),
                eventEntity.getAnnotation(),
                eventEntity.getConfirmedRequests(),
                eventDate,
                eventEntity.getPaid(),
                0,
                eventEntity.getCategory(),
                initiator);
    }
}
