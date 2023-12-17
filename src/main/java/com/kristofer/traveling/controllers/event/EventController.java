package com.kristofer.traveling.controllers.event;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kristofer.traveling.dtos.requests.event.EventRequest;
import com.kristofer.traveling.dtos.responses.event.EventResponse;
import com.kristofer.traveling.services.event.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping()
    public ResponseEntity<List<EventResponse>> findAll(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(eventService.findAll(authorizationHeader));
    }

    @GetMapping(value = "/my")
    public ResponseEntity<List<EventResponse>> findMyEvents(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(eventService.findMyEvents(authorizationHeader));
    }

    @GetMapping(value = "/myAttend")
    public ResponseEntity<List<EventResponse>> findMyEventsAttend(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(eventService.findMyEventsAttend(authorizationHeader));
    }

    @GetMapping(value = "/nowMonth")
    public ResponseEntity<List<EventResponse>> findEventsNowMonth(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(eventService.findEventsNowMonth(authorizationHeader));
    }

    @PostMapping()
    public ResponseEntity<EventResponse> insert(@RequestHeader("Authorization") String authorizationHeader, @RequestBody EventRequest request){
        return ResponseEntity.ok().body(eventService.insert(authorizationHeader, request));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<EventResponse> update(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id,  @RequestBody EventRequest request){
        return ResponseEntity.ok().body(eventService.update(authorizationHeader, request, id));
    }

    @PostMapping("/toogleAttend/{id}")
    public ResponseEntity<String> toogleAttendEvent(@PathVariable("id") Long id, @RequestHeader("Authorization") String authorizationHeader){
        eventService.toogleAttendEvent(authorizationHeader, id);
        return ResponseEntity.ok("Successfully attend!");
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        return ResponseEntity.ok().body(eventService.delete(authorizationHeader, id));
    }

}
