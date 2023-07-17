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
        eventToUpdate.setTitle(update.getTitle());
        eventToUpdate.setAnnotation(update.getAnnotation());
        eventToUpdate.setCategory(category);
        eventToUpdate.setDescription(update.getDescription());
        eventToUpdate.setEventDate(update.getEventDate());
        eventToUpdate.setLocationLat(update.getLocation().getLat());
        eventToUpdate.setLocationLon(update.getLocation().getLon());
        eventToUpdate.setPaid(update.isPaid());
        eventToUpdate.setParticipantLimit(update.getParticipantLimit());
        eventToUpdate.setRequestModeration(update.isRequestModeration());
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
        eventToUpdate.setTitle(update.getTitle());
        eventToUpdate.setAnnotation(update.getAnnotation());
        eventToUpdate.setCategory(category);
        eventToUpdate.setDescription(update.getDescription());
        eventToUpdate.setEventDate(update.getEventDate());
        eventToUpdate.setLocationLat(update.getLocation().getLat());
        eventToUpdate.setLocationLon(update.getLocation().getLon());
        eventToUpdate.setPaid(update.isPaid());
        eventToUpdate.setParticipantLimit(update.getParticipantLimit());
        eventToUpdate.setRequestModeration(update.isRequestModeration());
        if (update.getStateAction() == AdminEventState.REJECT_EVENT) {
            eventToUpdate.setState(EventStatus.CANCELED);
        } else if (update.getStateAction() == AdminEventState.PUBLISH_EVENT) {
            eventToUpdate.setState(EventStatus.PUBLISHED);
        }

        return eventToUpdate;
    }

    public EventFullDto toEventFullDto(EventEntity eventEntity,
                                       int views) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createdOn = eventEntity.getCreatedOn().format(formatter);
        String eventDate = eventEntity.getEventDate().format(formatter);
        String publishedOn = eventEntity.getPublishedOn().format(formatter);
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
                eventEntity.getInitiator(),
                location,
                views);
    }

    public EventShortDto toEventShortDto(EventEntity eventEntity,
                                         int views) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String eventDate = eventEntity.getEventDate().format(formatter);
        UserShortDto initiator = UserMapper.toUserShortDto(eventEntity.getInitiator());

        return new EventShortDto(eventEntity.getId(),
                eventEntity.getTitle(),
                eventEntity.getAnnotation(),
                eventEntity.getConfirmedRequests(),
                eventDate,
                eventEntity.getPaid(),
                views,
                eventEntity.getCategory(),
                initiator);
    }
}
