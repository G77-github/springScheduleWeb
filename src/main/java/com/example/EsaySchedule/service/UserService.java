package com.example.EsaySchedule.service;

import com.example.EsaySchedule.dto.AddUserRequest;
import com.example.EsaySchedule.entity.UserProfile;
import com.example.EsaySchedule.repository.EventJoinRepository;
import com.example.EsaySchedule.repository.TeamJoinRepository;
import com.example.EsaySchedule.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserProfileRepository userProfileRepository;
    private final TeamJoinRepository teamJoinRepository;
    private final EventJoinRepository eventJoinRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public Long saveUserProfile(AddUserRequest addUserRequest) {
        UserProfile userProfile = userProfileRepository.save(UserProfile.builder()
                .userName(addUserRequest.getUserName())
                .userEmail(addUserRequest.getUserEmail())
                .userPassword(bCryptPasswordEncoder.encode(addUserRequest.getUserPassword()))
                .isVerified(true)
                .userBan(false)
                .build());
        return userProfile.getUserId();
    }

    @Transactional
    public void updateUserName(Long userId, String userName) {


        Optional<UserProfile> byId = userProfileRepository.findById(userId);
        UserProfile userProfile = byId.get();
        userProfile.setUName(userName);
        userProfileRepository.save(userProfile);
    }

    @Transactional
    public void updatePassword(Long userId, String newPassword) {

        Optional<UserProfile> optionalUserProfile = userProfileRepository.findById(userId);
        UserProfile userProfile = optionalUserProfile.get();
        userProfile.setUserPassword(bCryptPasswordEncoder.encode(newPassword));

        userProfileRepository.save(userProfile);
    }

    @Transactional
    public void unregister(Long userId) {

        teamJoinRepository.deleteByUserId(userId);
        userProfileRepository.deleteById(userId);
        eventJoinRepository.deleteByUserId(userId);


    }
}
