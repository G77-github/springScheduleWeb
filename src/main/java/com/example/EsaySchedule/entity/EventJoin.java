package com.example.EsaySchedule.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_join")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(EventJoinPK.class)
@Getter
public class EventJoin {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "event_id")
    private Long eventId;


    @Builder
    public EventJoin(Long userId, Long eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }
}
