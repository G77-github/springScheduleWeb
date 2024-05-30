package com.example.EsaySchedule.dto;

import com.example.EsaySchedule.entity.Event;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String eventName;
    @NotBlank
    private String eventPlace;
    private String eventContent;
    @NotNull
    private LocalDateTime eventStart;
    @NotNull
    private LocalDateTime eventEnd;
    private LocalDateTime eventRegistration;
    @NotNull
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
