package com.kristofer.traveling.controllers.notification;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kristofer.traveling.dtos.responses.notification.NotificationAllResponse;
import com.kristofer.traveling.services.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping()
    public ResponseEntity<List<NotificationAllResponse>> notificationsByUser(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(notificationService.getAllNotificationsByUser(authorizationHeader));
    }

    @GetMapping(value = "/unread")
    public ResponseEntity<List<NotificationAllResponse>> unreadNotifications(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(notificationService.getAllUnreadNotifications(authorizationHeader));
    }

    @GetMapping(value = "/count")
    public ResponseEntity<Long> countUnreadNotifications(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(notificationService.countUnreadNotifications(authorizationHeader));
    }

    @PostMapping()
    public ResponseEntity<String> markNotificationAsRead(@RequestHeader("Authorization") String authorizationHeader, @RequestBody List<NotificationAllResponse> notifications){
        return ResponseEntity.ok().body(notificationService.markNotificationsAsRead(authorizationHeader, notifications));
    }
}
