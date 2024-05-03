package com.example.EsaySchedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateEventRequest {

    private String eventName;
    private String eventContent;
}
