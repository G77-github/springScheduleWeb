package com.example.EsaySchedule.service;

import com.example.EsaySchedule.dto.*;
import com.example.EsaySchedule.entity.*;
import com.example.EsaySchedule.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamJoinRepository teamJoinRepository;
    private final UserProfileRepository userProfileRepository;
    private final ImageRepository imageRepository;
    private final BookmarkRepository bookmarkRepository;

    public Team save(AddTeamRequest addTeamRequest) {
        return teamRepository.save(addTeamRequest.toEntity());
    }

    public List<Team> findAllTeam() {
        return teamRepository.findAll();
    }

    public List<TeamResponse> findUserTeams(Long userId) {
        List<Long> teamIds = teamJoinRepository.findApprovedAndNotBlockedTeamIdsByUserId(userId);
        List<Team> teamsByUserId = teamRepository.findByTeamIds(teamIds);
        List<TeamResponse> result = teamsByUserId.stream().map(TeamResponse::new).toList();

        return result;
    }

    public Optional<Team> findTeamByTeamId(Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        return team;
    }

    public void updateTeam(Long teamId, TeamManageRequest teamManageRequest) {
        Optional<Team> optionalTeam = teamRepository.findById(teamId);
        Team team = optionalTeam.get();
        String teamName = teamManageRequest.getTeamName();
        String teamDescription = teamManageRequest.getTeamDescription();
        Boolean isPublic = teamManageRequest.getIsPublic();

        log.info("변환전 데이터 이름 설명 퍼블릭 ={} {} {}", team.getTeamName(), team.getTeamDescription(), team.getIsPublic());
        log.info("변환할 데이터 이름 설명 퍼블릭 ={} {} {}", teamName, teamDescription, isPublic);
        team.update(teamName, teamDescription, isPublic);

        log.info("변환후 데이터 이름 설명 퍼블릭 ={} {} {}", team.getTeamName(), team.getTeamDescription(), team.getIsPublic());


        teamRepository.save(team);
    }

    public List<UserLabelResponse> waitingToJoinUser(Long teamId) {
        List<Long> userIds = teamJoinRepository.findNotApprovalAndNotBlockUserIdsByTeamId(teamId);
        List<UserProfile> users = userProfileRepository.findByUserIdsAndIsVerifiedTrueAndUserBanFalse(userIds);

        List<UserLabelResponse> result = users.stream().map(UserLabelResponse::new).toList();

        return result;
    }

    public List<UserLabelResponse> findTeamUsers(Long teamId) {
        List<Long> userIds = teamJoinRepository.findApprovedAndNotBlockUserIdsByTeamId(teamId);
        List<UserProfile> users = userProfileRepository.findByUserIdsAndIsVerifiedTrueAndUserBanFalse(userIds);

        List<UserLabelResponse> result = users.stream().map(UserLabelResponse::new).toList();

        return result;
    }

    public List<UserLabelResponse> findBlockUsers(Long teamId) {
        List<Long> blockUserIds = teamJoinRepository.findBlockUsersByTeamId(teamId);
        List<UserProfile> blockUsers = userProfileRepository.findByUserIdsAndIsVerifiedTrueAndUserBanFalse(blockUserIds);
        List<UserLabelResponse> result = blockUsers.stream().map(UserLabelResponse::new).toList();

        return result;
    }

    public List<TeamResponse> findTeamByKeyword(String keyword) {
        List<Team> teamByKeyword = teamRepository.findTeamByKeyword(keyword);
        List<TeamResponse> result = teamByKeyword.stream().map(TeamResponse::new).toList();

        return result;
    }

    @Transactional
    public void joinTeam(Long userId, Long teamId) {
        TeamJoin teamJoin = TeamJoin.builder()
                                    .userId(userId)
                                    .teamId(teamId)
                                    .approval(false)
                                    .userBlock(false)
                                    .build();
        teamJoinRepository.save(teamJoin);
    }

    @Transactional
    public void joinTeam(Long userId, Long teamId, boolean approval) {
        TeamJoin teamJoin = TeamJoin.builder()
                                    .userId(userId)
                                    .teamId(teamId)
                                    .approval(approval)
                                    .userBlock(false)
                                    .build();
        teamJoinRepository.save(teamJoin);
    }

    public void joinTeamAutoApproval(Long userId, Long teamId) {
        TeamJoin teamJoin = TeamJoin.builder()
                                    .userId(userId)
                                    .teamId(teamId)
                                    .approval(true)
                                    .userBlock(false)
                                    .build();

        teamJoinRepository.save(teamJoin);
    }

    public void approvalUser(Long userId, Long teamId) {
        TeamJoin teamJoin = teamJoinRepository.findByUserIdAndTeamId(userId, teamId);
        teamJoin.setApproval(true);
        teamJoinRepository.save(teamJoin);
    }

    public void blockTeamUser(Long userId, Long teamId) {
        TeamJoin teamJoin = teamJoinRepository.findByUserIdAndTeamId(userId, teamId);
        teamJoin.setUserBlock(true);
        teamJoinRepository.save(teamJoin);
    }

    public void unblockTeamUser(Long userId, Long teamId) {
        TeamJoin teamJoin = teamJoinRepository.findByUserIdAndTeamId(userId, teamId);
        teamJoin.setUserBlock(false);
        teamJoinRepository.save(teamJoin);
    }

    @Transactional
    public void teamExit(Long userId, Long teamId) {
        teamJoinRepository.updateUserBlockTrue(userId, teamId);

    }

    @Transactional
    public void transferMaster(Long teamId, Long newMasterId) {
        Optional<Team> optionalTeam = teamRepository.findById(teamId);
        Team team = optionalTeam.get();
        team.setTeamMasterId(newMasterId);
        teamRepository.save(team);
    }

    public void imageInfoSave(Long userId, Long teamId, Long eventId, String url) {
        Image image = Image.builder()
                .teamId(teamId)
                .userId(userId)
                .eventId(eventId)
                .imageUrl(url)
                .imageDate(LocalDateTime.now())
                .build();
        imageRepository.save(image);
    }

    public List<ImageResponse> findImage(Long teamId, Long eventId) {

        List<Image> imageList = imageRepository.findByTeamIdAndEventId(teamId, eventId);

        List<ImageResponse> imageResponseList = new ArrayList<>();

        for (Image image : imageList) {

            Optional<UserProfile> userProfile = userProfileRepository.findById(image.getUserId());
            String userName;
            if (userProfile.isEmpty()) {
                userName = "(알수 없음)";
            } else {
                userName = userProfile.get().getUName();
            }


            ImageResponse imageResponse = new ImageResponse(image, userName);
            imageResponseList.add(imageResponse);
        }

        return imageResponseList;

    }

    public boolean deleteImage(Long imageId) {

        if (!imageRepository.existsById(imageId)) {
            return false;
        }

        imageRepository.deleteById(imageId);
        return true;
    }


    public Long findTeamMasterId(Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        Long teamMasterId = team.get().getTeamMasterId();

        return teamMasterId;
    }


    public void addBookmark(Long userId, Long teamId, String teamName) {
        Bookmark bookmark = new Bookmark(userId, teamId, teamName);

        bookmarkRepository.save(bookmark);

    }

    @Transactional
    public void deleteBookmark(Long userId, Long teamId) {

        bookmarkRepository.deleteById(new BookmarkPK(userId, teamId));

    }

    public List<Bookmark> findUserBookMarks(Long userId) {
        List<Bookmark> bookmarkByUserId = bookmarkRepository.findBookmarkByUserId(userId);
        return bookmarkByUserId;
    }

    public Optional<Bookmark> findBookMarkByUserIdAndTeamId(Long userId, Long teamId) {
        return bookmarkRepository.findById(new BookmarkPK(userId, teamId));
    }

}
