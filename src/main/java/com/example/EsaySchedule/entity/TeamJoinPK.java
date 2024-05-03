package com.example.EsaySchedule.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class TeamJoinPK implements Serializable {

    private Long teamId;
    private Long userId;
}
