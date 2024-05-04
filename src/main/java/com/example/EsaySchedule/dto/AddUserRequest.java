package com.example.EsaySchedule.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userPasswordCheck;
}
