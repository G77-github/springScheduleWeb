package com.example.EsaySchedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamManageRequest {

    @NotBlank
    private String teamName;
    @NotBlank
    private String teamDescription;
    @NotNull
    private Boolean isPublic;
}
