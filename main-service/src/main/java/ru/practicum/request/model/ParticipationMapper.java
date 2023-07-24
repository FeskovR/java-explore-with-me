package ru.practicum.request.model;

import lombok.experimental.UtilityClass;
import ru.practicum.event.model.ParticipationEntity;
import ru.practicum.event.model.ParticipationRequestDto;

@UtilityClass
public class ParticipationMapper {
    public ParticipationRequestDto toParticipationRequestDto(ParticipationEntity participation) {
        return new ParticipationRequestDto(participation.getId(),
                participation.getEvent().getId(),
                participation.getCreated(),
                participation.getRequester().getId(),
                participation.getStatus());
    }
}
