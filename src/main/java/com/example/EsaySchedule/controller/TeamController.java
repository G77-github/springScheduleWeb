package com.example.EsaySchedule.controller;

import com.example.EsaySchedule.dto.*;
import com.example.EsaySchedule.entity.Bookmark;
import com.example.EsaySchedule.entity.Event;
import com.example.EsaySchedule.entity.Team;
import com.example.EsaySchedule.entity.UserProfile;
import com.example.EsaySchedule.service.EventService;
import com.example.EsaySchedule.service.TeamService;
import com.example.EsaySchedule.service.ValidationService;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Controller
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final EventService eventService;
    private final ValidationService validationService;

    @Value("${file.dir}")
    private String fileDir;


    @GetMapping("/")
    public String redirectMain(){
        return "redirect:/main";
    }
    @GetMapping("/main") //5/6
    public String viewMain(Model model) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        Long userId = currentUser.getUserId();
        String userName = currentUser.getUName();

        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);

        List<Bookmark> userBookMarks = teamService.findUserBookMarks(userId);
        model.addAttribute("userBookMarks", userBookMarks);


        //참가 선택한 이벤트
        List<EventAndTeamLabelResponse> userJoinTeamEvent = eventService.findEventsAndTeamsByUserId(userId);
        model.addAttribute("userJoinEvents", userJoinTeamEvent);

        //가입한 팀 새로운 이벤트
        List<EventAndTeamLabelResponse> joinTeamNewEvent = eventService.findNewEvent(userId);
        model.addAttribute("newEvents", joinTeamNewEvent);

        //가입한 그룹
        List<TeamResponse> joinTeams = teamService.findUserTeams(userId);
        model.addAttribute("joinTeams", joinTeams);


        //전체 팀 출력(일반인 쓸일 없고 웹 관리자만 쓸것 삭제하기 애매해서 둠)(기억)
/*        List<TeamResponse> teams = teamService.findAllTeam().stream()
                .map(TeamResponse::new)
                .toList();

        model.addAttribute("teams", teams);*/

        return "main";
    }

    @GetMapping("/team/{teamId}") //5/6
    public String viewTeamPage(@PathVariable(name = "teamId") Long teamId, Model model) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        Long userId = currentUser.getUserId();
        String userName = currentUser.getUName();
        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);

        List<Bookmark> userBookMarks = teamService.findUserBookMarks(userId);
        model.addAttribute("userBookMarks", userBookMarks);

        boolean userTeamJoinCheck = validationService.userTeamJoinCheck(userId, teamId);
        if (userTeamJoinCheck) {
            return "redirect:/main";
        }

        Optional<Team> teamByTeamId = teamService.findTeamByTeamId(teamId);
        model.addAttribute("teamId", teamId);
        model.addAttribute("teamName", teamByTeamId.get().getTeamName());

        LocalDateTime now = LocalDateTime.now();

        List<Event> futureEventByTeamId = eventService.findFutureEventByTeamId(teamId, now);
        List<EventLabelResponse> futureEventLabels = eventService.EventToEventLabel(futureEventByTeamId);

        model.addAttribute("futureEventLabels", futureEventLabels);

        Boolean bookmark = false;
        Optional<Bookmark> bookMarkByUserIdAndTeamId = teamService.findBookMarkByUserIdAndTeamId(userId, teamId);
        if (bookMarkByUserIdAndTeamId.isPresent()) {
            bookmark = true;
        }

        if (userId.equals(teamByTeamId.get().getTeamMasterId())) {
            model.addAttribute("isMaster", true);
        }

        model.addAttribute("bookmark", bookmark);
        model.addAttribute("showLeaveTeamLink", true);


        return "teamMain";
    }

    @GetMapping("/team/{teamId}/teamManage") //5/6
    public String viewTeamManage(@PathVariable(name = "teamId") Long teamId, Model model) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        Long userId = currentUser.getUserId();
        String userName = currentUser.getUName();
        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("teamId", teamId);
        List<Bookmark> userBookMarks = teamService.findUserBookMarks(userId);
        model.addAttribute("userBookMarks", userBookMarks);

        Long masterId = teamService.findTeamMasterId(teamId);


        if (!userId.equals(masterId)) {
            return "redirect:/team/" + teamId;
        }


        Optional<Team> optionalTeam = teamService.findTeamByTeamId(teamId);
        Team team = optionalTeam.get();

        model.addAttribute("team", team);
        log.info(" {} ",team.getTeamName());

        return "manageTeamTeam";
    }

    @PostMapping("/team/{teamId}/teamManage") //5/6
    public String teamManage(@PathVariable(name = "teamId") Long teamId, @Valid @ModelAttribute TeamManageRequest teamManageRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "error";
        }

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        Long userId = currentUser.getUserId();
        Long masterId = teamService.findTeamMasterId(teamId);


        if (!userId.equals(masterId)) {
            return "redirect:/team/" + teamId;
        }

        teamService.updateTeam(teamId, teamManageRequest);

        return "redirect:/team/" + teamId;
    }

    @GetMapping("/team/{teamId}/userManage") //5/6
    public String userManage(@PathVariable(name = "teamId") Long teamId, Model model) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        Long userId = currentUser.getUserId();
        String userName = currentUser.getUName();
        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        List<Bookmark> userBookMarks = teamService.findUserBookMarks(userId);
        model.addAttribute("userBookMarks", userBookMarks);

        Long masterId = teamService.findTeamMasterId(teamId);

        if (!userId.equals(masterId)) {
            return "redirect:/team/" + teamId;
        }

        List<UserLabelResponse> waitingToJoinUsers = teamService.waitingToJoinUser(teamId);

        for (UserLabelResponse i : waitingToJoinUsers) {
            log.info(i.getUserName());
        }

        model.addAttribute("waitingUsers", waitingToJoinUsers);

        model.addAttribute("teamId", teamId);
        Optional<Team> teamByTeamId = teamService.findTeamByTeamId(teamId);
        model.addAttribute("teamName", teamByTeamId.get().getTeamName());

        List<UserLabelResponse> teamUsers = teamService.findTeamUsers(teamId);
        model.addAttribute("teamUsers", teamUsers);

        List<UserLabelResponse> blockUsers = teamService.findBlockUsers(teamId);
        model.addAttribute("blockUsers", blockUsers);

        return "manageTeamUser";
    }

    @GetMapping("/search") //5/6
    public String viewSearch(@RequestParam(value = "keyword", required = false) String keyword, Model model) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        Long userId = currentUser.getUserId();
        String userName = currentUser.getUName();



        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        List<Bookmark> userBookMarks = teamService.findUserBookMarks(userId);
        model.addAttribute("userBookMarks", userBookMarks);

        if (keyword == null || keyword.trim().isEmpty()) {
            return "search";
        }

        List<TeamResponse> teams = teamService.findTeamByKeyword(keyword);

        model.addAttribute("teams", teams);

        return "search";
    }


    @GetMapping("/createTeam") //5/6
    public String createTeam(Model model) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        Long userId = currentUser.getUserId();
        String userName = currentUser.getUName();

        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        List<Bookmark> userBookMarks = teamService.findUserBookMarks(userId);
        model.addAttribute("userBookMarks", userBookMarks);

        return "createTeam";
    }

    @PostMapping("/createTeam") //5/6
    public String createTeam(@Valid @ModelAttribute AddTeamRequest addTeamRequest, BindingResult result) {

        if (result.hasErrors()) {
            return "error";
        }

        UserProfile currentUser = validationService.getUserData();
        if (currentUser == null) {
            return "error";
        }
        Long userId = currentUser.getUserId();
        addTeamRequest.setTeamMasterId(userId);

        Team newTeam = teamService.save(addTeamRequest);

        Long teamId = newTeam.getTeamId();
        teamService.joinTeam(userId, teamId, true);

        return "redirect:/main";
    }

    @GetMapping("/team/join") //5/6
    @ResponseBody
    public ResponseEntity<?> joinTeam(@RequestParam("teamId") Long teamId) {

        UserProfile currentUser = validationService.getUserData();

        Long userId = currentUser.getUserId();

        boolean preSubscriptionCheck = validationService.preSubscriptionCheck(userId, teamId);

        if (preSubscriptionCheck) {
            return ResponseEntity.badRequest().build();
        }

        //지금은 그냥 넘어가는데 teamId로 team테이블 조회해서 자동 가입승인 값 확인하고 확이하면
        //joinTeamAuto써야함 참고로 Db테이블도 값 수정해야함 지금은 그냥 넘어가게 만들어둠
        teamService.joinTeam(userId, teamId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/team/approval") //5/6
    @ResponseBody
    public ResponseEntity<?> approvalWaitingUser(@RequestParam("teamId") Long teamId, @RequestParam("waitingUserId") Long waitingUserId) {


        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }

        Long userId = currentUser.getUserId();
        Long masterId = teamService.findTeamMasterId(teamId);

        if (!userId.equals(masterId)) {
            return ResponseEntity.notFound().build();
        }

        teamService.approvalUser(waitingUserId, teamId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/team/block") //5/6
    @ResponseBody
    public ResponseEntity<?> blockTeamUser(@RequestParam("teamId") Long teamId, @RequestParam("teamUserId") Long teamUserId) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }

        Long userId = currentUser.getUserId();
        Long masterId = teamService.findTeamMasterId(teamId);

        if (!userId.equals(masterId)) {
            return ResponseEntity.badRequest().build();
        }
        if (masterId.equals(teamUserId)) {
            return ResponseEntity.badRequest().build();
        }

        teamService.blockTeamUser(teamUserId, teamId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/team/unblock") //5/6
    @ResponseBody
    public ResponseEntity<?> unblockTeamUser(@RequestParam("teamId") Long teamId, @RequestParam("teamUserId") Long teamUserId) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }

        Long userId = currentUser.getUserId();
        Long masterId = teamService.findTeamMasterId(teamId);

        if (!userId.equals(masterId)) {
            return ResponseEntity.notFound().build();
        }

        teamService.unblockTeamUser(teamUserId, teamId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/team/{teamId}/exit")
    public String exitTeam(@PathVariable("teamId") Long teamId) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        Long userId = currentUser.getUserId();

        Long teamMasterId = teamService.findTeamMasterId(teamId);

        if (userId.equals(teamMasterId)) {
            return "error";
        }

        teamService.teamExit(userId, teamId);

        return "redirect:/main";
    }

    @GetMapping("/team/{teamId}/userManage/masterTransfer")
    public ResponseEntity<?> masterTransfer(@PathVariable("teamId") Long teamId, @RequestParam("newMasterId") Long newMasterId) {

        UserProfile currentUser = validationService.getUserData();
        Long userId = currentUser.getUserId();
        Long teamMasterId = teamService.findTeamMasterId(teamId);
        if (!userId.equals(teamMasterId)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            teamService.transferMaster(teamId, newMasterId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/team/{teamId}/pastEvent")
    public String viewPastEvent(@PathVariable("teamId") Long teamId,
                                @RequestParam(value = "year", required = false) Integer year,
                                @RequestParam(value = "month", required = false) Integer month,
                                Model model) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        Long userId = currentUser.getUserId();
        String userName = currentUser.getUName();

        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        List<Bookmark> userBookMarks = teamService.findUserBookMarks(userId);
        model.addAttribute("userBookMarks", userBookMarks);

        Optional<Team> teamByTeamId = teamService.findTeamByTeamId(teamId);
        model.addAttribute("teamName", teamByTeamId.get().getTeamName());
        model.addAttribute("teamId", teamId);


        if (year == null && month == null) {
            return "viewPastEvent";
        }
        if (year != null && month == null) {
            month = 0;
        }

        LocalDateTime now = LocalDateTime.now();

        List<Event> events = eventService.findEventByDate(teamId, year, month, now);
        List<EventLabelResponse> eventLabelResponses = eventService.EventToEventLabel(events);

        model.addAttribute("eventLabels", eventLabelResponses);

        return "viewPastEvent";
    }

    @GetMapping("/team/{teamId}/imageBoard")
    public String viewImages(@PathVariable("teamId") Long teamId, @RequestParam(value = "eventId") Long eventId, Model model) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        Long userId = currentUser.getUserId();
        String userName = currentUser.getUName();
        model.addAttribute("userName", userName);
        List<Bookmark> userBookMarks = teamService.findUserBookMarks(userId);
        model.addAttribute("userBookMarks", userBookMarks);

        Optional<Team> teamByTeamId = teamService.findTeamByTeamId(teamId);
        model.addAttribute("teamName", teamByTeamId.get().getTeamName());

        model.addAttribute("teamId", teamId);
        model.addAttribute("eventId", eventId);
        Event eventById = eventService.findEventById(eventId);
        model.addAttribute("eventName", eventById.getEventName());

        List<ImageResponse> imageResponseList = teamService.findImage(teamId, eventId);

        model.addAttribute("imageList", imageResponseList);

        return "viewEventImage";
    }

    @PostMapping("/uploadImage")
    @ResponseBody
    public ResponseEntity<?> uploadImage(@RequestParam("images") MultipartFile[] images,
                                         @RequestParam("teamId") Long teamId,
                                         @RequestParam("eventId") Long eventId) throws IOException {


        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }

        Long userId = currentUser.getUserId();
        List<String> uploadFilePaths = new ArrayList<>();

        for (MultipartFile file : images) {
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String fullPath = fileDir + fileName;
                log.info("filePath={}", fullPath);
                file.transferTo(new File(fullPath));

                String webPath = "/images/" + fileName;
                uploadFilePaths.add(webPath);

                teamService.imageInfoSave(userId, teamId, eventId, webPath);

            }
        }
        return ResponseEntity.ok().body(uploadFilePaths);
    }

    @DeleteMapping("/deleteImage/{imageId}")
    @ResponseBody
    public ResponseEntity<?> deleteImage(@PathVariable("imageId") Long imageId) {

        boolean isDelete = teamService.deleteImage(imageId);

        if (isDelete) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/team/bookmark")
    @ResponseBody
    public ResponseEntity<?> addBookmark(@RequestParam("teamId") Long teamId, @RequestParam("teamName") String teamName) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }

        Long userId = currentUser.getUserId();

        teamService.addBookmark(userId, teamId, teamName);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/team/bookmark")
    @ResponseBody
    public ResponseEntity<?> deleteBookmark(@RequestParam("teamId")Long teamId) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }

        Long userId = currentUser.getUserId();

        teamService.deleteBookmark(userId, teamId);

        return ResponseEntity.ok().build();
    }
}
