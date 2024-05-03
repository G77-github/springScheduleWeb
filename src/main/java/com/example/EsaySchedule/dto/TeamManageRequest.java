package com.example.EsaySchedule.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamManageRequest {

    private String teamName;
    private String teamDescription;
    private Boolean isPublic;
}
