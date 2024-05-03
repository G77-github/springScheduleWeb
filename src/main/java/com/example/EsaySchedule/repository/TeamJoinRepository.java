package com.example.EsaySchedule.repository;

import com.example.EsaySchedule.entity.TeamJoin;
import com.example.EsaySchedule.entity.TeamJoinPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamJoinRepository extends JpaRepository<TeamJoin, TeamJoinPK> {

    @Query("SELECT tj.teamId FROM TeamJoin tj WHERE tj.userId = :userId AND tj.approval = true AND tj.userBlock = false")
    List<Long> findApprovedAndNotBlockedTeamIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT tj.userId FROM TeamJoin tj WHERE tj.teamId = :teamId AND tj.approval = false AND tj.userBlock = false")
    List<Long> findNotApprovalAndNotBlockUserIdsByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT tj.userId FROM TeamJoin tj WHERE tj.teamId = :teamId AND tj.approval = true AND tj.userBlock = false")
    List<Long> findApprovedAndNotBlockUserIdsByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT tj.userId FROM TeamJoin tj WHERE tj.teamId = :teamId AND userBlock = true")
    List<Long> findBlockUsersByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT tj FROM TeamJoin tj WHERE tj.userId = :userId AND tj.teamId = :teamId")
    TeamJoin findByUserIdAndTeamId(@Param("userId") Long userId, @Param("teamId") Long teamId);

    @Query("DELETE FROM TeamJoin tj WHERE tj.userId = :userId AND tj.teamId = :teamId")
    void deleteByUserIdAndTeamId(@Param("userId") Long userId, @Param("teamId") Long teamId);

}
