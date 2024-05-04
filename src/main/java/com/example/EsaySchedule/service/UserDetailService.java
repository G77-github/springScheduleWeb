package com.example.EsaySchedule.service;

import com.example.EsaySchedule.entity.UserProfile;
import com.example.EsaySchedule.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailService implements UserDetailsService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public UserProfile loadUserByUsername(String userEmail) {

        return userProfileRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException((userEmail)));
    }

}
