package ru.practicum.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.error.exception.BreakingRulesException;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.event.enums.EventRequestStatus;
import ru.practicum.event.enums.EventStatus;
import ru.practicum.event.model.EventEntity;
import ru.practicum.event.model.ParticipationEntity;
import ru.practicum.event.model.ParticipationRequestDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.request.model.ParticipationMapper;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.UserDto;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    RequestRepository requestRepository;
    UserRepository userRepository;
    EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getUserRequests(int userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User id " + userId + " not found")
        );

        List<ParticipationEntity> requests = requestRepository.findAllByRequesterId(userId);
        List<ParticipationRequestDto> result = new ArrayList<>();

        for (ParticipationEntity request : requests) {
            result.add(ParticipationMapper.toParticipationRequestDto(request));
        }

        return result;
    }

    @Override
    public ParticipationRequestDto addNewRequest(int userId, int eventId) {
        UserDto userDto = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User id " + userId + " not found")
        );

        EventEntity event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event id " + eventId + " not found")
        );

        if (event.getInitiator().getId() == userId) {
            throw new BreakingRulesException("Initiator cannot request to own event");
        }

        if (event.getState() != EventStatus.PUBLISHED) {
            throw new BreakingRulesException("Event is not published");
        }

        if (event.getParticipantLimit() != 0 &&
            event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new BreakingRulesException("Limit of participant");
        }

        Optional<ParticipationEntity> checkedParticipation = requestRepository.findRequestByEventIdAndRequesterId(
                eventId,
                userId
        );
        if (checkedParticipation.isPresent()) {
            throw new BreakingRulesException("Request from user " + userId + " to event id " + eventId +
                    " is already exist");
        }

        ParticipationEntity participation = new ParticipationEntity(0,
                event,
                LocalDateTime.now(),
                userDto,
                EventRequestStatus.PENDING);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            participation.setStatus(EventRequestStatus.CONFIRMED);
        }

        if (!event.getRequestModeration()) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        eventRepository.save(event);
        ParticipationEntity returnedParticipationEntity = requestRepository.save(participation);

        return ParticipationMapper.toParticipationRequestDto(returnedParticipationEntity);
    }

    @Override
    public ParticipationRequestDto cancelUserRequest(int userId, int requestId) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User id " + userId + " not found")
        );

        ParticipationEntity request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Request not found")
        );

        request.setStatus(EventRequestStatus.CANCELED);

        ParticipationEntity updated = requestRepository.save(request);

        return ParticipationMapper.toParticipationRequestDto(updated);
    }
}
