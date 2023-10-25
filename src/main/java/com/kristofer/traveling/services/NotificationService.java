package com.kristofer.traveling.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.responses.notification.NotificationAllResponse;
import com.kristofer.traveling.models.NotificationModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.models.Enums.NotificationTypeEnum;
import com.kristofer.traveling.repositories.NotificationRepository;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;
import com.kristofer.traveling.services.exceptions.ObjectNotPermission;
import com.kristofer.traveling.services.users.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public void createNotification(UserModel user, NotificationTypeEnum type, UserModel creator, Long activityId){
        if(!(user.getId() == creator.getId())){
            var notification = NotificationModel.builder()
            .user(user)
            .type(type)
            .activityId(activityId)
            .creator(creator)
            .read(false)
            .build();
        notificationRepository.save(notification);
        }
    }

    public List<NotificationAllResponse> getAllNotificationsByUser(String token){
        List<NotificationModel> notificationsModel = notificationRepository.findByUserIdOrderByCreatedAtDesc(userService.userByToken(token).getId());
        return notificationsModel.stream().map(x-> new NotificationAllResponse(x))
        .collect(Collectors.toList());
        
    }

    public List<NotificationAllResponse> getAllUnreadNotifications(String token) {
        List<NotificationModel> notificationsModel = notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userService.userByToken(token).getId());
        return notificationsModel.stream().map(x-> new NotificationAllResponse(x))
        .collect(Collectors.toList());  
    }

    public String markNotificationsAsRead(String token, List<NotificationAllResponse> notifications) {
        List<Long> notificationIds = notifications.stream()
            .map(NotificationAllResponse::getId)
            .collect(Collectors.toList());
        
        List<NotificationModel> notificationModels = notificationRepository.findAllById(notificationIds);
        
        for (NotificationModel notificationModel : notificationModels) {
            this.verifyIdUser(token, notificationModel.getUser().getId());
            notificationModel.setRead(true);
        }
        
        notificationRepository.saveAll(notificationModels);
        
        return "Read of Success!";
    }

    // public String markNotificationAsRead(String token, Long notificationId){
    //     NotificationModel notification = notificationRepository.findById(notificationId)
    //         .orElseThrow(() -> new ObjectNotFoundException("Notification with id " + notificationId + " not found"));
    //     this.verifyIdUser(token, notification.getUser().getId());
    //     notification.setRead(true);
    //     notificationRepository.save(notification);
    //     return "Read of Sucess!";
    // }

    public long countUnreadNotifications(String token) {
        return notificationRepository.countByUserIdAndReadFalse(userService.userByToken(token).getId());
    }

    private void verifyIdUser(String token, Long id){
        UserModel user = userService.userByToken(token);
        if(user.getId() != id){
            throw new ObjectNotPermission("This user does not have permission to change Post");
        }
    }

}
