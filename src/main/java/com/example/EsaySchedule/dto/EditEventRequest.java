package com.example.EsaySchedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EditEventRequest {

    private Long eventId;
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

}
