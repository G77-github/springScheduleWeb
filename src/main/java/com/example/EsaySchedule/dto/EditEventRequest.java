package com.example.EsaySchedule.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EditEventRequest {

    private Long eventId;
    private String eventName;
    private String eventPlace;
    private String eventContent;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
    private LocalDateTime eventRegistration;
    private Boolean notEvent;

}
