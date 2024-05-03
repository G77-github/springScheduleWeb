package com.example.EsaySchedule.controller;

import com.example.EsaySchedule.dto.*;
import com.example.EsaySchedule.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/createEvent")
    public String createEvent() {

        return "addSchedule";
    }

    @PostMapping("/createEvent")
    public String createEvent(@ModelAttribute AddEventRequest eventRequest) {

        log.info("userId = {}", eventRequest.getUserId());
        log.info("teamId={}", eventRequest.getTeamId());
        log.info("evnetName={}", eventRequest.getEventName());
        log.info("eventContent={}", eventRequest.getEventContent());
        log.info("place={}", eventRequest.getEventPlace());
        log.info("start={}", eventRequest.getEventStart());
        log.info("end={}", eventRequest.getEventEnd());
        log.info("notevent={}", eventRequest.getNotEvent());

        if (eventRequest.getEventStart().isAfter(eventRequest.getEventEnd())) {
            log.info("종료일이 시작일보다 빠름");
            return "redirect:/createEvent";
        }
        eventRequest.setEventRegistration(LocalDateTime.now());
        eventService.save(eventRequest);

        return "redirect:/createEvent";

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

        Long userId = 1L; //임시값

        EventDetailResponse eventDetail = eventService.findEventDetail(eventId, userId);

        model.addAttribute("eventDetail", eventDetail);
        model.addAttribute("teamId", teamId);

        List<EventParticipantsResponse> participants = eventService.findEventParticipants(eventId);

        model.addAttribute("participants", participants);

        Boolean userParticipateCheck = eventService.userParticipateCheck(eventId, userId);
        model.addAttribute("userParticipate", userParticipateCheck);
        log.info("유저 참가 확인={}", userParticipateCheck);

        model.addAttribute("userId", userId);


        return "viewEvent";
    }

    @DeleteMapping("/team/{teamId}/event")
    @ResponseBody
    public ResponseEntity<?> deleteEvent(@PathVariable(name = "teamId") Long teamId, @RequestParam(name = "eventId") Long eventId) {

        try {
            eventService.deleteEvent(eventId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("이벤트 삭제중 오류 발생");
        }
    }

    @GetMapping("/team/{teamId}/eventEdit")
    public String editEvent(@PathVariable(name = "teamId") Long teamId, @RequestParam(name = "eventId") Long eventId, Model model) {

        Long userId = 1L; //임시값
        EventDetailResponse eventDetail = eventService.findEventDetail(eventId);
        model.addAttribute("eventDetail", eventDetail);
        model.addAttribute("teamId", teamId);

        return "editEvent";

    }

    @PostMapping("/team/{teamId}/eventEdit")
    public String editEvent(@PathVariable(name = "teamId") Long teamId, @RequestParam(name = "eventId") Long eventId, @ModelAttribute EditEventRequest eventRequest) {

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

        //참가 로직 구현
        log.info("참가~~~~~~");
        log.info("userId={}, eventId={}", participationRequest.getUserId(), participationRequest.getEventId());
        Long userId = participationRequest.getUserId();
        Long eventId = participationRequest.getEventId();

        eventService.saveEventJoin(userId, eventId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/event/participate")
    @ResponseBody
    public ResponseEntity<?> removeParticipation(@RequestBody ParticipationRequest participationRequest) {

        //참가 취소 로직 구현
        log.info("참가취소~~~~~~~");
        log.info("userId={}, eventId={}", participationRequest.getUserId(), participationRequest.getEventId());

        Long userId = participationRequest.getUserId();
        Long eventId = participationRequest.getEventId();

        eventService.deleteEventJoin(userId, eventId);

        return ResponseEntity.ok().build();
    }
}
