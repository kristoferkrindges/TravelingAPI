package com.kristofer.traveling.dtos.responses.notification;

import java.util.Date;
import java.util.UUID;

import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.NotificationModel;
import com.kristofer.traveling.models.Enums.NotificationTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NotificationAllResponse {
    private UUID id;
    private UserAllResponse user;
    private UserAllResponse creator;
    private UUID activityId;
    private NotificationTypeEnum type;
    private boolean read;
    private Date time;

    public NotificationAllResponse(){}

    public NotificationAllResponse(NotificationModel notificationModel){
        this.id = notificationModel.getId();
        this.user = new UserAllResponse(notificationModel.getUser());
        this.creator = new UserAllResponse(notificationModel.getCreator());
        this.activityId = notificationModel.getActivityId();
        this.type = notificationModel.getType();
        this.read = notificationModel.isRead();
        this.time = notificationModel.getCreatedAt();
    }
}
