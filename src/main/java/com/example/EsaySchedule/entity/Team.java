package com.example.EsaySchedule.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "team_description")
    private String teamDescription;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "team_master_id")
    private Long teamMasterId;

    @Builder
    public Team(String teamName, String teamDescription, Boolean isPublic, Long teamMasterId) {
        this.teamName = teamName;
        this.teamDescription = teamDescription;
        this.isPublic = isPublic;
        this.teamMasterId = teamMasterId;
    }

    public void update(String teamName, String teamDescription, Boolean isPublic) {
        this.teamName = teamName;
        this.teamDescription = teamDescription;
        this.isPublic = isPublic;
    }

    public void setTeamMasterId(Long teamMasterId) {
        this.teamMasterId = teamMasterId;
    }
}
