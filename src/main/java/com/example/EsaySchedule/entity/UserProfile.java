package com.example.EsaySchedule.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user_profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long userId;

    @Column(name = "user_name")
    private String uName;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "user_ban")
    private Boolean userBan;


    @Builder
    public UserProfile(String userName, String userPassword, String userEmail, Boolean isVerified, Boolean userBan, String auth) {
        this.uName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.isVerified = isVerified;
        this.userBan = userBan;
    }

    public UserProfile(UserProfile userProfile, String newUName) {
        this.userId = userProfile.getUserId();
        this.uName = newUName;
        this.userPassword = userProfile.getUserPassword();
        this.userEmail = userProfile.getUserEmail();
        this.isVerified = userProfile.getIsVerified();
        this.userBan = userProfile.userBan;
    }

    public static UserProfile emptyUser() {
        return UserProfile.builder().userName("(알수 없음)").build();
    }

    public void setUName(String uName) {
        this.uName = uName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return userEmail;
    }


    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
