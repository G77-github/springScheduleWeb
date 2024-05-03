package com.example.EsaySchedule.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "user_ban")
    private Boolean userBan;


    @Builder
    public UserProfile(String userName, String userPassword, String userEmail, Boolean isVerified, Boolean userBan) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.isVerified = isVerified;
        this.userBan = userBan;
    }
}
