package com.example.EsaySchedule.dto;


import com.example.EsaySchedule.entity.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddTeamRequest {

    @NotBlank(message = "팀 이름은 필수입니다")
    private String teamName;

    @NotBlank(message = "팀 설명은 필수입니다")
    private String teamDescription;

    @NotNull
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



