package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.EventEntity;
import ru.practicum.event.model.EventFullDto;

import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {
    @Query("select e from EventEntity as e " +
            "where e.initiator.id = :id")
    List<EventEntity> findByInitiatorId(@Param("id") int id, Pageable pageable);
}
