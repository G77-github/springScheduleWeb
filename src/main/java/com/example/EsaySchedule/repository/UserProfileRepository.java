package com.example.EsaySchedule.repository;

import com.example.EsaySchedule.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("SELECT u FROM UserProfile u WHERE u.userId IN :userIds AND u.isVerified = TRUE AND u.userBan = FALSE")
    List<UserProfile> findByUserIdsAndIsVerifiedTrueAndUserBanFalse(@Param("userIds") List<Long> userIds);

}