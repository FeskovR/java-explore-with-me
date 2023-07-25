package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Participation;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Participation, Integer> {
    List<Participation> findAllByRequesterId(int requesterId);

    List<Participation> findAllByEventId(int eventId);

    @Query("select pe from Participation as pe " +
            "where pe.requester = :userId and pe.event = :eventId")
    Optional<Participation> findUserRequest(@Param("userId") int userId,
                                            @Param("eventId") int eventId);

    @Query("select pe from Participation as pe " +
            "join pe.event as e " +
            "where e.id = :eventId")
    List<Participation> findEventOwnerRequests(@Param("eventId") int eventID);

    @Query("select pe from Participation as pe " +
            "where pe.id in :ids")
    List<Participation> findRequestsByIds(@Param("ids") List<Integer> ids);

    @Query("select pe from Participation as pe " +
            "where pe.event.id = :eventId and pe.requester.id = :userId")
    Optional<Participation> findRequestByEventIdAndRequesterId(@Param("eventId") int eventId,
                                                               @Param("userId") int userId);
}
