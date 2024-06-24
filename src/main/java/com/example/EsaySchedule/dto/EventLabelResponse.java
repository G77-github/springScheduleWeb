package com.example.EsaySchedule.dto;

import com.example.EsaySchedule.entity.Event;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
public class EventLabelResponse {

    private Long eventId;
    private String eventName;
    private String eventPlace;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;

    public EventLabelResponse(Event event) {
        this.eventId = event.getEventId();
        this.eventName = event.getEventName();
        this.eventPlace = event.getEventPlace();
        this.eventStart = event.getEventStart();
        this.eventEnd = event.getEventEnd();
    }
}
