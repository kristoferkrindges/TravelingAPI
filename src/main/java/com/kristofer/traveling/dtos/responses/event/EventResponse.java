package com.kristofer.traveling.dtos.responses.event;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserLikePost;
import com.kristofer.traveling.models.EventModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EventResponse {
    private UUID id;
    private String name;
    private LocalDateTime eventDate;
    private String city;
    private String address;
    private String zipCode;
    private String photo;
    private Integer attends;
    private String details;
    private boolean pressAttend;
    private String price;
    private String type;
    private UserAllResponse userAllResponse;
    private List<UserLikePost> usersAttends;
    private boolean edit;

    public EventResponse(EventModel event){
        this.id = event.getId();
        this.name = event.getName();
        this.eventDate = event.getEventDate();
        this.city = event.getCity();
        this.address = event.getAddress();
        this.zipCode = event.getZipCode();
        this.photo = event.getPhoto();
        this.price = event.getPrice();
        this.details = event.getDetails();
        this.type = event.getType();
        this.attends = event.getAttend() != null ? event.getAttend().size() : 0;
        this.userAllResponse = new UserAllResponse(event.getCreator());
        this.pressAttend = false;
        this.edit = event.isEdit();
    }

    public EventResponse(EventModel event, boolean pressAttend, List<UserLikePost> usersAttends){
        this.id = event.getId();
        this.name = event.getName();
        this.eventDate = event.getEventDate();
        this.city = event.getCity();
        this.address = event.getAddress();
        this.zipCode = event.getZipCode();
        this.photo = event.getPhoto();
        this.type = event.getType();
        this.attends = event.getAttend() != null ? event.getAttend().size() : 0;
        this.userAllResponse = new UserAllResponse(event.getCreator());
        this.price = event.getPrice();
        this.details = event.getDetails();
        this.pressAttend = pressAttend;
        this.usersAttends = usersAttends;
        this.edit = event.isEdit();
    }
}
