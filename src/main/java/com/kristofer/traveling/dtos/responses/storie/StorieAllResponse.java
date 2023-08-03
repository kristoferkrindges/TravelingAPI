package com.kristofer.traveling.dtos.responses.storie;

import java.util.Date;

import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.StorieModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StorieAllResponse {
    private Long id;
    private String video;
    private Date datepublic;
    private UserAllResponse userAllResponse;

    public StorieAllResponse(StorieModel storie){
        this.id = storie.getId();
        this.video = storie.getVideo();
        this.datepublic = storie.getDatePublic();
        this.userAllResponse = new UserAllResponse(storie.getCreator());
    }

}
