package com.example.EsaySchedule.service;

import com.example.EsaySchedule.entity.Team;
import com.example.EsaySchedule.entity.TeamJoin;
import com.example.EsaySchedule.entity.UserProfile;
import com.example.EsaySchedule.repository.TeamJoinRepository;
import com.example.EsaySchedule.repository.TeamRepository;
import com.example.EsaySchedule.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationService {

    private final TeamJoinRepository teamJoinRepository;
    private final UserProfileRepository userProfileRepository;
    private final TeamRepository teamRepository;

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

    public void updateSession(Long userId, String newUserName) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        Optional<UserProfile> byId = userProfileRepository.findById(userId);
        UserProfile userProfile = new UserProfile(byId.get(), newUserName);
        Authentication newAuth = new UsernamePasswordAuthenticationToken(userProfile, currentAuth.getCredentials(), currentAuth.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);

    }

    public boolean checkUserEmilaDuplicate(String userEmail) {
        Optional<UserProfile> byUserEmail = userProfileRepository.findByUserEmail(userEmail);

        if (byUserEmail.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean preSubscriptionCheck(Long userId, Long teamId) {

        TeamJoin byUserIdAndTeamId = teamJoinRepository.findByUserIdAndTeamId(userId, teamId);

        if (byUserIdAndTeamId == null) {
            return false;
        }

        return true;

    }


    public boolean isTeamMaster(Long userId) {

        List<Team> byUserId = teamRepository.findByUserId(userId);

        if (byUserId.isEmpty()) {
            return false;
        }
        return true;

    }
}
