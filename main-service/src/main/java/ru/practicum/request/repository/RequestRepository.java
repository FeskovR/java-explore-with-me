package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.ParticipationEntity;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationEntity, Integer> {
    List<ParticipationEntity> findAllByRequesterId(int requesterId);

    List<ParticipationEntity> findAllByEventId(int eventId);

    @Query("select pe from ParticipationEntity as pe " +
            "where pe.requester = :userId and pe.event = :eventId")
    Optional<ParticipationEntity> findUserRequest(@Param("userId") int userId,
                                                  @Param("eventId") int eventId);

    @Query("select pe from ParticipationEntity as pe " +
            "join pe.event as e " +
            "where e.id = :eventId")
    List<ParticipationEntity> findEventOwnerRequests(@Param("eventId") int eventID);

    @Query("select pe from ParticipationEntity as pe " +
            "where pe.id in :ids")
    List<ParticipationEntity> findRequestsByIds(@Param("ids") List<Integer> ids);

    @Query("select pe from ParticipationEntity as pe " +
            "where pe.event.id = :eventId and pe.requester.id = :userId")
    Optional<ParticipationEntity> findRequestByEventIdAndRequesterId(@Param("eventId") int eventId,
                                                                     @Param("userId") int userId);
}
