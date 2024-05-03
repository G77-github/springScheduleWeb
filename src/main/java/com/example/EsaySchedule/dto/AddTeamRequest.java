package com.example.EsaySchedule.dto;


import com.example.EsaySchedule.entity.Team;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddTeamRequest {

    private String teamName;
    private String teamDescription;
    private Boolean isPublic;
    private Long teamMasterId;


    public Team toEntity() {
        return Team.builder()
                .teamName(teamName)
                .teamDescription(teamDescription)
                .isPublic(isPublic)
                .teamMasterId(teamMasterId)
                .build();

    }
}



