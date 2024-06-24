package com.example.EsaySchedule.repository;

import com.example.EsaySchedule.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByTeamId(Long teamId);

    @Query("SELECT e FROM Event e WHERE e.teamId =:teamId AND e.eventStart > :now AND e.notEvent = FALSE")
    List<Event> findFutureEventByTeamIdAndNotEventFalse(@Param("teamId")Long teamId, @Param("now")LocalDateTime now);

    @Query("SELECT e FROM Event e WHERE e.teamId =:teamId AND e.eventStart < :now AND e.notEvent = FALSE")
    List<Event> findPastEventByTeamIdAndNotEventFalse(@Param("teamId")Long teamId, @Param("now")LocalDateTime now);


    @Query("SELECT e FROM Event e WHERE e.teamId IN :teamIds AND e.eventStart > :now AND e.notEvent = FALSE")
    List<Event> findFutureEventByTeamIdAndNotEventFalse(@Param("teamIds") List<Long> teamIds, @Param("now") LocalDateTime now);

    @Query("SELECT e FROM Event e WHERE e.teamId =:teamId AND YEAR(e.eventStart) =:year AND MONTH(e.eventStart) =:month AND e.eventStart <:now AND e.notEvent = FALSE")
    List<Event> findByYearAndMonth(@Param("teamId")Long teamId ,@Param("year") Integer year, @Param("month") Integer month, @Param("now")LocalDateTime now);

    @Query("SELECT e FROM Event e WHERE e.teamId =:teamId AND YEAR(e.eventStart) =:year AND e.eventStart <:now AND e.notEvent = FALSE")
    List<Event> findByYear(@Param("teamId")Long teamId, @Param("year") Integer year, @Param("now")LocalDateTime now);

    @Query("SELECT e FROM Event e WHERE e.eventId =:eventId AND e.eventStart >:now AND e.notEvent = FALSE")
    Optional<Event> findFutureEventById(@Param("eventId") Long eventId, @Param("now") LocalDateTime now);

    List<Event> findByEventIdIn(List<Long> eventIds);

    List<Event> findByTeamIdAndNotEventFalse(Long teamId);

    List<Event> findByTeamIdAndNotEventTrue(Long teamId);

    @Modifying
    void deleteByTeamId(Long teamId);
}
