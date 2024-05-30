package com.example.EsaySchedule.entity;

import com.example.EsaySchedule.dto.EditEventRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", updatable = false)
    private Long eventId;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_place")
    private String eventPlace;

    @Column(name = "event_content")
    private String eventContent;

    @Column(name = "event_start")
    private LocalDateTime eventStart;

    @Column(name = "event_end")
    private LocalDateTime eventEnd;

    @Column(name = "event_registration")
    private LocalDateTime eventRegistration;

    @Column(name = "notevent")
    private Boolean notEvent;

    @Builder
    public Event(Long teamId, Long userId, String eventName,String eventPlace , String eventContent, LocalDateTime eventStart, LocalDateTime eventEnd, LocalDateTime eventRegistration, Boolean notEvent) {
        this.teamId = teamId;
        this.userId = userId;
        this.eventName = eventName;
        this.eventPlace = eventPlace;
        this.eventContent = eventContent;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventRegistration = eventRegistration;
        this.notEvent = notEvent;
    }

    public void update(String eventName, String eventContent) {
        this.eventName = eventName;
        this.eventContent = eventContent;
    }

    public void update(EditEventRequest eventRequest) {
        this.eventName = eventRequest.getEventName();
        this.eventPlace = eventRequest.getEventPlace();
        this.eventContent = eventRequest.getEventContent();
        this.eventStart = eventRequest.getEventStart();
        this.eventEnd = eventRequest.getEventEnd();
        this.eventRegistration = eventRequest.getEventRegistration();
        this.notEvent = eventRequest.getNotEvent();
    }
}
