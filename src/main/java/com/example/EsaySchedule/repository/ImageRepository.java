package com.example.EsaySchedule.repository;

import com.example.EsaySchedule.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i WHERE i.teamId =:teamId AND i.eventId =:eventId")
    public List<Image> findByTeamIdAndEventId(@Param("teamId") Long teamId, @Param("eventId") Long eventId);

    @Modifying
    public void deleteByTeamId(Long teamId);
}
