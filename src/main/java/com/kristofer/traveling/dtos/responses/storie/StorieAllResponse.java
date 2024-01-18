package com.kristofer.traveling.dtos.responses.storie;

import java.util.Date;
import java.util.UUID;

import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.StorieModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StorieAllResponse {
    private UUID id;
    private String video;
    private Date datepublic;
    private Integer likes;
    private boolean pressLike;
    private UserAllResponse userAllResponse;
    private boolean edit;

    public StorieAllResponse(StorieModel storie){
        this.id = storie.getId();
        this.video = storie.getVideo();
        this.datepublic = storie.getDatePublic();
        this.likes = storie.getLikes() != null ? storie.getLikes().size() : 0;
        this.pressLike = false;
        this.userAllResponse = new UserAllResponse(storie.getCreator());
        this.edit = false;
    }

    public StorieAllResponse(StorieModel storie, boolean pressLike){
        this.id = storie.getId();
        this.video = storie.getVideo();
        this.datepublic = storie.getDatePublic();
        this.likes = storie.getLikes() != null ? storie.getLikes().size() : 0;
        this.pressLike = pressLike;
        this.userAllResponse = new UserAllResponse(storie.getCreator());
        this.edit = false;
    }

}
