package com.example.EsaySchedule.service;

import com.example.EsaySchedule.dto.AddUserRequest;
import com.example.EsaySchedule.entity.UserProfile;
import com.example.EsaySchedule.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserProfileRepository userProfileRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public Long saveUserProfile(AddUserRequest addUserRequest) {
        UserProfile userProfile = userProfileRepository.save(UserProfile.builder()
                .userName(addUserRequest.getUserName())
                .userEmail(addUserRequest.getUserEmail())
                .userPassword(bCryptPasswordEncoder.encode(addUserRequest.getUserPassword()))
                .build());
        return userProfile.getUserId();
    }

}
