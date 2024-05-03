package com.example.EsaySchedule.controller;

import com.example.EsaySchedule.dto.*;
import com.example.EsaySchedule.entity.Event;
import com.example.EsaySchedule.entity.Team;
import com.example.EsaySchedule.service.EventService;
import com.example.EsaySchedule.service.TeamService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Slf4j
@Controller
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final EventService eventService;

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/main")
    public String viewMain(Model model) {

        Long userId = 1L; //나중에 세션에서 받아오는걸로 수정

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

    @GetMapping("/team/{teamId}")
    public String viewTeamPage(@PathVariable(name = "teamId") Long teamId, Model model) {

        log.info("teamId={}", teamId);

        List<Event> events = eventService.findEventByTeamId(teamId);
        List<EventLabelResponse> eventLabels = eventService.EventToEventLabel(events);

        model.addAttribute("eventLabels", eventLabels);

        return "teamHome";
    }

    @GetMapping("/team/{teamId}/teamManage")
    public String viewTeamManage(@PathVariable(name = "teamId") Long teamId, Model model) {

        Optional<Team> optionalTeam = teamService.findTeamByTeamId(teamId);
        Team team = optionalTeam.get();

        model.addAttribute("team", team);

        return "manageTeamTeam";
    }

    @PostMapping("/team/{teamId}/teamManage")
    public String teamManage(@PathVariable(name = "teamId") Long teamId, @ModelAttribute TeamManageRequest teamManageRequest) {
        teamService.updateTeam(teamId, teamManageRequest);

        return "redirect:/team/" + teamId + "/teamManage";
    }

    @GetMapping("/team/{teamId}/userManage")
    public String userManage(@PathVariable(name = "teamId") Long teamId, Model model) {

        List<UserLabelResponse> waitingToJoinUsers = teamService.waitingToJoinUser(teamId);
        model.addAttribute("waitingUsers", waitingToJoinUsers);
        log.info("가입대기 데이터 확인 ={}", waitingToJoinUsers.getLast().getUserName());

        model.addAttribute("teamId", teamId);

        List<UserLabelResponse> teamUsers = teamService.findTeamUsers(teamId);
        model.addAttribute("teamUsers", teamUsers);

        List<UserLabelResponse> blockUsers = teamService.findBlockUsers(teamId);
        model.addAttribute("blockUsers", blockUsers);
        log.info("차단 회원 확인={}", blockUsers.getFirst().getUserName());

        return "manageTeamUser";
    }

    @GetMapping("/search")
    public String viewSearch(@RequestParam(value = "keyword", required = false) String keyword, Model model) {

        List<TeamResponse> teams = teamService.findTeamByKeyword(keyword);

        model.addAttribute("teams", teams);

        return "search";
    }


    @GetMapping("/createTeam")
    public String createTeam() {

        return "createTeam";
    }

    @PostMapping("/createTeam")
    public String createTeam(@ModelAttribute AddTeamRequest addTeamRequest) {
        log.info("teamName={}", addTeamRequest.getTeamName());
        log.info("teamDescription={}", addTeamRequest.getTeamDescription());
        log.info("teamIspublic={}", addTeamRequest.getIsPublic());
        log.info("teamMasterId={}", addTeamRequest.getTeamMasterId());

        teamService.save(addTeamRequest);

        return "redirect:/createTeam";
    }

    @PostMapping("/team/join")
    @ResponseBody
    public ResponseEntity<?> joinTeam(@RequestParam("teamId") Long teamId) {

        log.info("넘어온 팀 아이디 확인과 데이터 전송 확인={}", teamId);
        Long userId = 7L; //임시값 나중에 세션에서 가져와야함
        //지금은 그냥 넘어가는데 teamId로 team테이블 조회해서 자동 가입승인 값 확인하고 확이하면
        //joinTeamAuto써야함 참고로 Db테이블도 값 수정해야함 지금은 그냥 넘어가게 만들어둠
        teamService.joinTeam(userId, teamId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/team/approval")
    @ResponseBody
    public ResponseEntity<?> approvalWaitingUser(@RequestParam("teamId") Long teamId, @RequestParam("waitingUserId") Long waitingUserId) {

        log.info("넘어온 데이터 확인 user={}, team={}", waitingUserId, teamId);

        teamService.approvalUser(waitingUserId, teamId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/team/block")
    @ResponseBody
    public ResponseEntity<?> blockTeamUser(@RequestParam("teamId") Long teamId, @RequestParam("teamUserId") Long teamUserId) {

        teamService.blockTeamUser(teamUserId, teamId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/team/unblock")
    @ResponseBody
    public ResponseEntity<?> unblockTeamUser(@RequestParam("teamId") Long teamId, @RequestParam("teamUserId") Long teamUserId) {

        teamService.unblockTeamUser(teamUserId, teamId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/team/{teamId}/exit")
    public String exitTeam(@PathVariable("teamId") Long teamId) {
        Long userId = 3L; //나중에 세션에서 가져와야함

        teamService.teamExit(userId, teamId);

        return "redirect:/main";
    }

    @GetMapping("/team/{teamId}/userManage/masterTransfer")
    public String masterTransfer(@PathVariable("teamId")Long teamId, @RequestParam("newMasterId") Long newMasterId) {

        teamService.transferMaster(teamId, newMasterId);

        return "redirect:/team/" + teamId;
    }

    @GetMapping("/team/{teamId}/imageBoard")
    public String viewImageBoard(@PathVariable("teamId") Long teamId,
                                 @RequestParam(value = "year", required = false)Integer year,
                                 @RequestParam(value = "month", required = false)Integer month ,
                                 Model model) {

        model.addAttribute("teamId", teamId);

        if (year == null && month == null) {
            return "imageBoard";
        }
        if (year != null && month == null) {
            month = 0;
        }

        List<Event> events = eventService.findEventByDate(teamId, year, month);
        List<EventLabelResponse> eventLabelResponses = eventService.EventToEventLabel(events);

        model.addAttribute("eventLabels", eventLabelResponses);

        return "imageBoard";
    }

    @GetMapping("/team/{teamId}/images")
    public String viewImages(@PathVariable("teamId") Long teamId, @RequestParam(value = "eventId") Long eventId, Model model) {

        model.addAttribute("teamId", teamId);
        model.addAttribute("eventId", eventId);
        Event eventById = eventService.findEventById(eventId);
        model.addAttribute("eventName", eventById.getEventName());

        List<ImageResponse> imageResponseList = teamService.findImage(teamId, eventId);

        model.addAttribute("imageList", imageResponseList);

        log.info("imageId ={}", imageResponseList.getFirst().getImageId());


        return "viewImage";
    }

    @PostMapping("/uploadImage")
    @ResponseBody
    public void uploadImage(@RequestParam("file") MultipartFile file,
                              @RequestParam("teamId") Long teamId,
                              @RequestParam("eventId") Long eventId) throws IOException {

        Long userId = 1L; //임시값

        if (!file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String fullPath = fileDir + fileName;
            log.info("filePath={}", fullPath);
            file.transferTo(new File(fullPath));

            String webPath = "/images/" + fileName;

            teamService.ImageInfoSave(userId, teamId, eventId, webPath);
        }

        log.info("파일저장컨트롤러 완료");
    }

    @DeleteMapping("/deleteImage/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable("imageId") Long imageId) {

        boolean isDelete = teamService.deleteImage(imageId);

        if (isDelete) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
