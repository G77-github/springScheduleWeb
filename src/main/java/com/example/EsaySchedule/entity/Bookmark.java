package com.example.EsaySchedule.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookmark")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(BookmarkPK.class)
@Getter
public class Bookmark {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "team_name")
    private String teamName;

    public Bookmark(Long userId, Long teamId, String teamName) {
        this.userId = userId;
        this.teamId = teamId;
        this.teamName = teamName;
    }
}
