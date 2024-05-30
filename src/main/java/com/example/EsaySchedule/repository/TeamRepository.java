package com.example.EsaySchedule.repository;

import com.example.EsaySchedule.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t FROM Team t WHERE teamId IN :teamIds")
    List<Team> findByTeamIds(@Param("teamIds") List<Long> teamIds);

    @Query("SELECT t FROM Team t WHERE t.isPublic = true AND t.teamName LIKE %:keyword%")
    List<Team> findTeamByKeyword(@Param("keyword") String keyword);

    @Query("SELECT t FROM Team t WHERE t.teamMasterId =:userId")
    List<Team> findByUserId(@Param("userId") Long userId);


}
