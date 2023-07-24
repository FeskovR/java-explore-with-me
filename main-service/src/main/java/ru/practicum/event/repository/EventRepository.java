package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.enums.EventStatus;
import ru.practicum.event.model.EventEntity;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {
    @Query("select e from EventEntity as e " +
            "where e.initiator.id = :id")
    List<EventEntity> findByInitiatorId(@Param("id") int id, Pageable pageable);

    @Query("select e from EventEntity as e " +
            "where e.state = :state and e.id = :id ")
    Optional<EventEntity> getEventByIdPublic(@Param("id") int id,
                                             @Param("state") EventStatus state);

    @Query("select e from EventEntity as e " +
            "where e.state = 'PUBLISHED'")
    List<EventEntity> findAllPublished();

    @Query("select e from EventEntity as e " +
            "where e.id in :ids")
    List<EventEntity> findAllByIds(@Param("ids") List<Integer> ids);

    @Query("select e from EventEntity as e " +
            "where e.category.id = :eventId")
    List<EventEntity> findAllEventsByCategoryId(@Param("eventId") int eventId);
}
