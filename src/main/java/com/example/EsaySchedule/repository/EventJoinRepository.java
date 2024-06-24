package com.example.EsaySchedule.repository;

import com.example.EsaySchedule.entity.EventJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventJoinRepository extends JpaRepository<EventJoin, Long> {

    List<EventJoin> findByUserId(Long userId);

    @Query("SELECT ej.eventId FROM EventJoin ej WHERE ej.userId =:userId")
    List<Long> findEventIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT ej.userId FROM EventJoin ej WHERE ej.eventId = :eventId")
    List<Long> findUserIdByEventId(@Param("eventId") Long eventId);

    @Modifying
    @Query("DELETE FROM EventJoin ej WHERE ej.eventId = :eventId")
    void deleteByEventId(@Param("eventId") Long eventId);

    Optional<EventJoin> findByEventIdAndUserId(Long EventId, Long userId);

    @Modifying
    @Query("DELETE FROM EventJoin ej WHERE ej.userId = :userId AND ej.eventId = :eventId")
    void deleteByUserIdAndEventId(@Param("userId") Long userId,@Param("eventId") Long eventId);

    @Modifying
    @Query("DELETE FROM EventJoin ej WHERE ej.userId =:userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM EventJoin ej WHERE ej.eventId IN :eventIds")
    void deleteByEventIds(@Param("eventIds") List<Long> eventIds);
}
