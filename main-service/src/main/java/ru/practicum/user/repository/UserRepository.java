package ru.practicum.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.user.model.UserDto;

import java.util.List;

public interface UserRepository extends JpaRepository<UserDto, Integer> {
    @Query("select ud from UserDto as ud " +
            "where ud.id in :ids")
    Page<UserDto> getUsersByParams(@Param("ids") List<Integer> ids, Pageable pageable);
}
