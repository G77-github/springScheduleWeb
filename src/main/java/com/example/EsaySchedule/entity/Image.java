package com.example.EsaySchedule.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id", updatable = false)
    private Long imgId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "img_url")
    private String imageUrl;

    @Column(name = "img_date")
    private LocalDateTime imageDate;

    @Builder
    public Image(Long userId, Long teamId, Long eventId, String imageUrl, LocalDateTime imageDate) {
        this.userId = userId;
        this.teamId = teamId;
        this.eventId = eventId;
        this.imageUrl = imageUrl;
        this.imageDate = imageDate;
    }
}
