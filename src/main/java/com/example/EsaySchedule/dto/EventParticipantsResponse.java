package com.example.EsaySchedule.dto;

import com.example.EsaySchedule.entity.UserProfile;
import lombok.Getter;

@Getter
public class EventParticipantsResponse {

    private Long userId;
    private String userName;


    public EventParticipantsResponse(UserProfile userProfile) {
        this.userId = userProfile.getUserId();
        this.userName = userProfile.getUserName();
    }
}
