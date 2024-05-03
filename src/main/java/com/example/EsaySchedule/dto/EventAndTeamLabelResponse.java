package com.example.EsaySchedule.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventAndTeamLabelResponse {

    private Long teamId;
    private String teamName;
    private Long eventId;
    private String eventName;
    private LocalDateTime eventStart;
    private String eventPlace;
}
