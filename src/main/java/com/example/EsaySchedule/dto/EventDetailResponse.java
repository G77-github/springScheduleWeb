package com.example.EsaySchedule.dto;

import com.example.EsaySchedule.entity.Event;
import com.example.EsaySchedule.entity.UserProfile;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventDetailResponse {
    private Long eventId;
    private String eventName;
    private String eventPlace;
    private String eventContent;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
    private Boolean notEvent;
    private String userName;


    public EventDetailResponse(Event event, UserProfile userProfile) {
        this.eventId = event.getEventId();
        this.eventName = event.getEventName();
        this.eventPlace = event.getEventPlace();
        this.eventContent = event.getEventContent();
        this.eventStart = event.getEventStart();
        this.eventEnd = event.getEventEnd();
        this.userName = userProfile.getUName();
    }

    public EventDetailResponse(Event event) {
        this.eventId = event.getEventId();
        this.eventName = event.getEventName();
        this.eventPlace = event.getEventPlace();
        this.eventContent = event.getEventContent();
        this.eventStart = event.getEventStart();
        this.eventEnd = event.getEventStart();
        this.notEvent = event.getNotEvent();
    }
}
