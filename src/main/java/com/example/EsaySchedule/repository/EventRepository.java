package com.example.EsaySchedule.repository;

import com.example.EsaySchedule.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByTeamId(Long teamId);

    List<Event> findByTeamIdAndNotEventFalse(Long teamId);

    @Query("SELECT e FROM Event e WHERE e.teamId IN :teamIds AND e.notEvent = FALSE")
    List<Event> findByTeamIdAndNotEventFalse(@Param("teamIds") List<Long> teamIds);

    @Query("SELECT e FROM Event e WHERE e.teamId =:teamId AND YEAR(e.eventStart) =:year AND MONTH(e.eventStart) =:month AND e.notEvent = FALSE")
    List<Event> findByYearAndMonth(@Param("teamId")Long teamId ,@Param("year") Integer year, @Param("month") Integer month);

    @Query("SELECT e FROM Event e WHERE e.teamId =:teamId AND YEAR(e.eventStart) =:year AND e.notEvent = FALSE")
    List<Event> findByYear(@Param("teamId")Long teamId, @Param("year") Integer year);
}
