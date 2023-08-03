package com.kristofer.traveling.dtos.responses.post;

import java.util.Date;

import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.PostModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PostAllResponse {
    private Long id;
    private String phrase;
    private String img;
    private Date datepublic;
    private UserAllResponse userAllResponse;

    public PostAllResponse(PostModel post){
        this.id = post.getId();
        this.phrase = post.getPhrase();
        this.img = post.getImg();
        this.datepublic = post.getDatePublic();
        this.userAllResponse = new UserAllResponse(post.getCreator());
    }
}
