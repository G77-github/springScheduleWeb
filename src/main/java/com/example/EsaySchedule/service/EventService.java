package com.example.EsaySchedule.service;

import com.example.EsaySchedule.dto.*;
import com.example.EsaySchedule.entity.Event;
import com.example.EsaySchedule.entity.EventJoin;
import com.example.EsaySchedule.entity.Team;
import com.example.EsaySchedule.entity.UserProfile;
import com.example.EsaySchedule.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final EventJoinRepository eventJoinRepository;
    private final TeamRepository teamRepository;
    private final TeamJoinRepository teamJoinRepository;
    private final UserProfileRepository userProfileRepository;

    public Event save(AddEventRequest request) {
        return eventRepository.save(request.toEntity());
    }

    public void updateEvent(EditEventRequest request) {
        Optional<Event> optionalEvent = eventRepository.findById(request.getEventId());
        Event eventToUpdate = optionalEvent.get();
        eventToUpdate.update(request);
        eventRepository.save(eventToUpdate);

    }

    public List<Event> findAllEventByTeamId(Long teamId) {
        return eventRepository.findByTeamId(teamId);
    }

    public List<Event> findEventByTeamId(Long teamId) {
        return eventRepository.findByTeamIdAndNotEventFalse(teamId);
    }

    public List<Event> findFutureEventByTeamId(Long teamId, LocalDateTime now) {
        return eventRepository.findFutureEventByTeamIdAndNotEventFalse(teamId, now);
    }

    public List<Event> findPastEventByTeamId(Long teamId, LocalDateTime now) {
        return eventRepository.findPastEventByTeamIdAndNotEventFalse(teamId, now);
    }

    public List<EventLabelResponse> EventToEventLabel(List<Event> events) {
        return events.stream().map(EventLabelResponse::new).toList();
    }

    public List<Long> findEventJoinByUserId(Long userId) {
        return eventJoinRepository.findEventIdsByUserId(userId);
    }

    public List<EventAndTeamLabelResponse> findEventsAndTeamsByUserId(Long userId) {
        List<EventJoin> eventJoins = eventJoinRepository.findByUserId(userId);
        List<EventAndTeamLabelResponse> result = new ArrayList<>();

        for (EventJoin join : eventJoins) {
            Event event = eventRepository.findFutureEventById(join.getEventId(), LocalDateTime.now()).orElse(null);
            if (event != null) {
                Team team = teamRepository.findById(event.getTeamId()).orElse(null);

                EventAndTeamLabelResponse dto = new EventAndTeamLabelResponse();
                dto.setTeamId(team.getTeamId());
                dto.setTeamName(team.getTeamName());
                dto.setEventId(event.getEventId());
                dto.setEventName(event.getEventName());
                dto.setEventStart(event.getEventStart());
                dto.setEventPlace(event.getEventPlace());
                result.add(dto);
                }
            }

        return result;

    }

    public List<EventAndTeamLabelResponse> findNewEvent(Long userId) {

        List<Long> teamIds = teamJoinRepository.findApprovedAndNotBlockedTeamIdsByUserId(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Event> eventsByTeamIds = eventRepository.findFutureEventByTeamIdAndNotEventFalse(teamIds, now);
        LocalDateTime twoDaysAgo = now.minusDays(2);
        List<Event> newEventsByTeamIds = eventsByTeamIds.stream().filter(event -> event.getEventRegistration().isAfter(twoDaysAgo)).toList();
        List<EventAndTeamLabelResponse> result = new ArrayList<>();

        for (Event event : newEventsByTeamIds) {
            Team team = teamRepository.findById(event.getTeamId()).orElse(null);

            EventAndTeamLabelResponse dto = new EventAndTeamLabelResponse();
            dto.setTeamId(team.getTeamId());
            dto.setTeamName(team.getTeamName());
            dto.setEventId(event.getEventId());
            dto.setEventName(event.getEventName());
            dto.setEventStart(event.getEventStart());
            dto.setEventPlace(event.getEventPlace());
            result.add(dto);
        }

        return result;

    }


    public EventDetailResponse findEventDetail(Long eventId, Long userId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        Event event = optionalEvent.get();
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findById(userId);
        UserProfile user = optionalUserProfile.get();

        return new EventDetailResponse(event, user);
    }

    public EventDetailResponse findEventDetail(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        Event event = optionalEvent.get();

        return new EventDetailResponse(event);
    }

    public List<EventParticipantsResponse> findEventParticipants(Long eventId) {

        log.info("이벤트id={}", eventId);
        List<Long> userIds = eventJoinRepository.findUserIdByEventId(eventId);

        if (userIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<UserProfile> participants = userProfileRepository.findByUserIdsAndIsVerifiedTrueAndUserBanFalse(userIds);
        List<EventParticipantsResponse> result = participants.stream().map(EventParticipantsResponse::new).toList();
        return result;
    }

    public Boolean userParticipateCheck(Long eventId, Long userId) {
        Optional<EventJoin> userParticipateCheck = eventJoinRepository.findByEventIdAndUserId(eventId, userId);
        if (userParticipateCheck.isEmpty()) {
            return false;
        }

        return true;

    }

    public EventJoin saveEventJoin(Long userId, Long eventId) {
        return eventJoinRepository.save(new EventJoin(userId, eventId));
    }

    @Transactional
    public void deleteEventJoin(Long userId, Long eventId) {
        eventJoinRepository.deleteByUserIdAndEventId(userId, eventId);

    }


    public List<Event> findAllEvent() {
        return eventRepository.findAll();
    }

    public Event findEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
        eventJoinRepository.deleteByEventId(eventId);
    }


    @Transactional
    public Event update(Long eventId, UpdateEventRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + eventId));

        event.update(request.getEventName(), request.getEventContent());
        
        return event;
    }

    public List<Event> findEventByDate(Long teamId,Integer year, Integer month, LocalDateTime now) {
        if (month == 0) {
            return eventRepository.findByYear(teamId, year, now);
        } else {
            return eventRepository.findByYearAndMonth(teamId, year, month, now);
        }

    }

    public List<Event> findEventsByUserId(List<Long> eventIds) {
        return eventRepository.findByEventIdIn(eventIds);
    }
}
