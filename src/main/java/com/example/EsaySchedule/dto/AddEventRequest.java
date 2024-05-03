package com.example.EsaySchedule.dto;

import com.example.EsaySchedule.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddEventRequest {

    private Long teamId;
    private Long userId;
    private String eventName;
    private String eventPlace;
    private String eventContent;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
    private LocalDateTime eventRegistration;
    private Boolean notEvent;

    public Event toEntity() {
        return Event.builder()
                .teamId(teamId)
                .userId(userId)
                .eventName(eventName)
                .eventPlace(eventPlace)
                .eventContent(eventContent)
                .eventStart(eventStart)
                .eventEnd(eventEnd)
                .eventRegistration(eventRegistration)
                .notEvent(notEvent)
                .build();
    }

}
