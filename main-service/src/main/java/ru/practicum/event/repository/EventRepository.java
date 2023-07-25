package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.enums.EventStatus;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query("select e from Event as e " +
            "where e.initiator.id = :id")
    List<Event> findByInitiatorId(@Param("id") int id, Pageable pageable);

    @Query("select e from Event as e " +
            "where e.state = :state and e.id = :id ")
    Optional<Event> getEventByIdPublic(@Param("id") int id,
                                       @Param("state") EventStatus state);

    @Query("select e from Event as e " +
            "where e.state = 'PUBLISHED'")
    List<Event> findAllPublished();

    @Query("select e from Event as e " +
            "where e.id in :ids")
    List<Event> findAllByIds(@Param("ids") List<Integer> ids);

    @Query("select e from Event as e " +
            "where e.category.id = :eventId")
    List<Event> findAllEventsByCategoryId(@Param("eventId") int eventId);
}
