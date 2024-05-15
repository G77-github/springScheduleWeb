package com.example.EsaySchedule.controller;

import com.example.EsaySchedule.dto.*;
import com.example.EsaySchedule.entity.Event;
import com.example.EsaySchedule.entity.EventJoin;
import com.example.EsaySchedule.entity.Team;
import com.example.EsaySchedule.entity.UserProfile;
import com.example.EsaySchedule.service.EventService;
import com.example.EsaySchedule.service.TeamService;
import com.example.EsaySchedule.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Slf4j
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final ValidationService validationService;
    private final TeamService teamService;

    @GetMapping("/team/{teamId}/createEvent")
    public String createEvent(@PathVariable("teamId")Long teamId, Model model) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        String userName = currentUser.getUName();
        model.addAttribute("userName", userName);

        Optional<Team> teamByTeamId = teamService.findTeamByTeamId(teamId);
        model.addAttribute("teamId", teamByTeamId.get().getTeamId());
        model.addAttribute("teamName", teamByTeamId.get().getTeamName());


        return "addEvent";
    }

    @PostMapping("/team/{teamId}/createEvent")
    public String createEvent(@PathVariable("teamId")Long teamId, @ModelAttribute AddEventRequest eventRequest) {

        log.info("teamId={}", eventRequest.getTeamId());
        log.info("evnetName={}", eventRequest.getEventName());
        log.info("eventContent={}", eventRequest.getEventContent());
        log.info("place={}", eventRequest.getEventPlace());
        log.info("start={}", eventRequest.getEventStart());
        log.info("end={}", eventRequest.getEventEnd());
        log.info("notevent={}", eventRequest.getNotEvent());

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        Long userId = currentUser.getUserId();
        eventRequest.setUserId(userId);

        if (validationService.userTeamJoinCheck(userId, teamId)) {
            log.info("팀에 가입되어있지 않은 유저임");
            return "error";
        }

        if (eventRequest.getEventStart().isAfter(eventRequest.getEventEnd())) {
            log.info("종료일이 시작일보다 빠름");
            return "error";
        }

        if (eventRequest.getEventName() == null || eventRequest.getEventStart() == null || eventRequest.getEventPlace() == null || eventRequest.getEventContent() == null) {
            log.info("필수값들이 입력되지 않음");
            return "error";
        }
        eventRequest.setEventRegistration(LocalDateTime.now());
        Event saveEvent = eventService.save(eventRequest);

        eventService.saveEventJoin(userId, saveEvent.getEventId());


        return "redirect:/team/" + teamId;

    }

/*    @GetMapping("/viewTeam")
    public String viewTeam(Model model) {

        List<EventResponse> events = eventService.findEventByTeamId(3L)
                .stream()
                .map(EventResponse::new)
                .toList();

        model.addAttribute("events", events);

        return "viewGroup";

    }*/


    @GetMapping("/team/{teamId}/event")
    public String viewEvent(@PathVariable(name = "teamId") Long teamId, @RequestParam("eventId") Long eventId, Model model) {


        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        Long userId = currentUser.getUserId();
        model.addAttribute("userId", userId);
        model.addAttribute("userName", currentUser.getUName());

        EventDetailResponse eventDetail = eventService.findEventDetail(eventId, userId);
        model.addAttribute("eventDetail", eventDetail);

        Optional<Team> teamByTeamId = teamService.findTeamByTeamId(teamId);

        model.addAttribute("teamName", teamByTeamId.get().getTeamName());
        model.addAttribute("teamId", teamId);

        List<EventParticipantsResponse> participants = eventService.findEventParticipants(eventId);

        model.addAttribute("participants", participants);

        Boolean userParticipateCheck = eventService.userParticipateCheck(eventId, userId);
        model.addAttribute("userParticipate", userParticipateCheck);



        return "viewEvent";
    }

    @DeleteMapping("/team/{teamId}/event")
    @ResponseBody
    public ResponseEntity<?> deleteEvent(@PathVariable(name = "teamId") Long teamId, @RequestParam(name = "eventId") Long eventId) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return ResponseEntity.badRequest().body("이벤트 삭제중 오류 발생");
        }

        Long userId = currentUser.getUserId();
        Event eventById = eventService.findEventById(eventId);
        if (!userId.equals(eventById.getUserId())) {
            return ResponseEntity.badRequest().body("작성자가 아님");
        }

        try {
            eventService.deleteEvent(eventId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("이벤트 삭제중 오류 발생");
        }
    }

    @GetMapping("/team/{teamId}/eventEdit")
    public String editEvent(@PathVariable(name = "teamId") Long teamId, @RequestParam(name = "eventId") Long eventId, Model model) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        Long userId = currentUser.getUserId();
        EventDetailResponse eventDetail = eventService.findEventDetail(eventId);

        log.info("오류위치 확인1");
        log.info("userId={}", userId.getClass().getName());
        log.info("EuserId={}", eventDetail.getUserId().getClass().getName());

        if (!userId.equals(eventDetail.getUserId())) {
            return "error";
        }

        model.addAttribute("eventDetail", eventDetail);
        model.addAttribute("teamId", teamId);

        return "editEvent";

    }

    @PostMapping("/team/{teamId}/eventEdit")
    public String editEvent(@PathVariable(name = "teamId") Long teamId, @RequestParam(name = "eventId") Long eventId, @ModelAttribute EditEventRequest eventRequest) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        Long userId = currentUser.getUserId();
        Event eventById = eventService.findEventById(eventId);
        if (!userId.equals(eventById.getUserId())) {
            return "error";
        }

        if (eventRequest.getEventStart().isAfter(eventRequest.getEventEnd())) {
            log.info("종료일이 시작일보다 빠름");
            return "redirect:/team/" + teamId + "/event?eventId=" + eventId;
        }
        eventRequest.setEventRegistration(LocalDateTime.now());
        eventService.updateEvent(eventRequest);

        return "redirect:/team/" + teamId + "/event?eventId=" + eventId;
    }

    @PostMapping("/event/participate")
    @ResponseBody
    public ResponseEntity<?> addParticipation(@RequestBody ParticipationRequest participationRequest) {


        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }

        Long userId = currentUser.getUserId();
        Long eventId = participationRequest.getEventId();

        Event eventById = eventService.findEventById(eventId);
        if (eventById == null) {
            return ResponseEntity.notFound().build();
        }
        Long teamId = eventById.getTeamId();

        if (validationService.userTeamJoinCheck(userId, teamId)) {
            return ResponseEntity.notFound().build();
        }

        eventService.saveEventJoin(userId, eventId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/event/participate")
    @ResponseBody
    public ResponseEntity<?> removeParticipation(@RequestBody ParticipationRequest participationRequest) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }

        Long userId = currentUser.getUserId();
        Long eventId = participationRequest.getEventId();

        Event eventById = eventService.findEventById(eventId);
        if (eventById == null) {
            return ResponseEntity.notFound().build();
        }
        Long teamId = eventById.getTeamId();

        if (validationService.userTeamJoinCheck(userId, teamId)) {
            return ResponseEntity.notFound().build();
        }

        eventService.deleteEventJoin(userId, eventId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/event/my")
    @ResponseBody
    public List<FullCalDto> findJoinEvent() {
        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return Collections.emptyList();
        }

        Long userId = currentUser.getUserId();
        List<Long> joinList = eventService.findEventJoinByUserId(userId);
        List<Event> joinEventList = eventService.findEventsByUserId(joinList);
        List<FullCalDto> eventData = joinEventList.stream().map(FullCalDto::new).toList();

        return eventData;

    }

    @GetMapping("/event/team/{teamId}")
    @ResponseBody
    public List<FullCalDto> findTeamEventAndJoin(@PathVariable("teamId")Long teamId) {
        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return Collections.emptyList();
        }
        Long userId = currentUser.getUserId();

        List<Event> events = eventService.findEventByTeamId(teamId);
        List<Long> joinList = eventService.findEventJoinByUserId(userId);
        Map<Boolean, List<Event>> splitEvent = events.stream()
                .collect(Collectors.partitioningBy(event -> joinList.contains(event.getEventId())));
        List<Event> joinTeamEvent = splitEvent.get(true);
        List<Event> notJoinTeamEvent = splitEvent.get(false);

        List<FullCalDto> list1 = joinTeamEvent.stream().map(event -> new FullCalDto(event, "rgb(33,37,41)")).toList();
        List<FullCalDto> list2 = notJoinTeamEvent.stream().map(event -> new FullCalDto(event, "rgb(127,127,127")).toList();

        List<FullCalDto> teamEventList = Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toList());
        return teamEventList;


    }
}
