package ru.practicum.request.model;

import lombok.experimental.UtilityClass;
import ru.practicum.event.model.Participation;
import ru.practicum.event.model.ParticipationRequestDto;

@UtilityClass
public class ParticipationMapper {
    public ParticipationRequestDto toParticipationRequestDto(Participation participation) {
        return new ParticipationRequestDto(participation.getId(),
                participation.getEvent().getId(),
                participation.getCreated(),
                participation.getRequester().getId(),
                participation.getStatus());
    }
}
