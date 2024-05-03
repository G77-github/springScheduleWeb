package com.example.EsaySchedule.dto;

import com.example.EsaySchedule.entity.Team;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TeamResponse {


    private Long teamId;
    private String teamName;
    private String teamDescription;
    private Long teamMasterId;

    public TeamResponse(Team team) {
        this.teamId = team.getTeamId();
        this.teamName = team.getTeamName();
        this.teamDescription = team.getTeamDescription();
        this.teamMasterId = team.getTeamMasterId();
    }
}
