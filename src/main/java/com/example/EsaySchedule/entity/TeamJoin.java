package com.example.EsaySchedule.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_join")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(TeamJoinPK.class)
public class TeamJoin {

    @Column(name = "team_id")
    @Id
    private Long teamId;
    @Column(name = "user_id")
    @Id
    private Long userId;
    @Column(name = "approval", nullable = false)
    private Boolean approval = false;
    @Column(name = "user_block", nullable = false)
    private Boolean userBlock = false;

    @Builder
    public TeamJoin(Long teamId, Long userId, Boolean approval, Boolean userBlock) {
        this.teamId = teamId;
        this.userId = userId;
        this.approval = approval;
        this.userBlock = userBlock;
    }

    public void setApproval(Boolean approval) {
        this.approval = approval;
    }

    public void setUserBlock(Boolean userBlock) {
        this.userBlock = userBlock;
    }
}
