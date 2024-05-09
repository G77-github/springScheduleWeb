package com.example.EsaySchedule.service;

import com.example.EsaySchedule.entity.Team;
import com.example.EsaySchedule.entity.TeamJoin;
import com.example.EsaySchedule.entity.UserProfile;
import com.example.EsaySchedule.repository.TeamJoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationService {

    private final TeamJoinRepository teamJoinRepository;

    public UserProfile getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserProfile) {
            return (UserProfile) authentication.getPrincipal();
        }
        return null;
    }

    public boolean userTeamJoinCheck(Long userId, Long teamId) {
        TeamJoin byUserIdAndTeamId = teamJoinRepository.findByUserIdAndTeamId(userId, teamId);
        if (byUserIdAndTeamId.getApproval() == true && byUserIdAndTeamId.getUserBlock() == false && byUserIdAndTeamId != null) {
            return false;
        }
        return true;
    }

}
