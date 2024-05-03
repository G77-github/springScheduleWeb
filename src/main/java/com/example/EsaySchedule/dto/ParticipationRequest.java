package com.example.EsaySchedule.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipationRequest {

    private Long userId;
    private Long eventId;
}
