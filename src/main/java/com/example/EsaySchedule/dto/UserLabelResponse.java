package com.example.EsaySchedule.dto;

import com.example.EsaySchedule.entity.UserProfile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLabelResponse {

    private Long userId;
    private String userName;
    private String userEmail;

    public UserLabelResponse(UserProfile userProfile) {
        this.userId = userProfile.getUserId();
        this.userName = userProfile.getUserName();
        this.userEmail = userProfile.getUserEmail();
    }
}
