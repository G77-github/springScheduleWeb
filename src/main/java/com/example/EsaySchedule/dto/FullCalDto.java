package com.example.EsaySchedule.dto;

import com.example.EsaySchedule.entity.Event;
import lombok.Getter;

@Getter
public class FullCalDto {
    private String id;
    private String title;
    private String start;
    private String end;
    private String backgroundColor;
    private String borderColor;

    public FullCalDto(Event event) {
        this.id = event.getEventId().toString();
        this.title = event.getEventName();
        this.start = event.getEventStart().toString();
        this.end = event.getEventEnd().toString();
        this.borderColor = "white";
        if (event.getNotEvent() == false) {
            this.backgroundColor = "rgb(33,37,41)";
        } else {
            this.backgroundColor = "red";
        }
    }

    public FullCalDto(Event event, String backgroundColor) {
        this.id = event.getEventId().toString();
        this.title = event.getEventName();
        this.start = event.getEventStart().toString();
        this.end = event.getEventEnd().toString();
        this.backgroundColor = backgroundColor;
        this.borderColor = "white";
    }
}
