package com.kristofer.traveling.dtos.responses.post;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserLikePost;
import com.kristofer.traveling.models.PostModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PostAllResponse {
    private UUID id;
    private String phrase;
    private String img;
    private Date datepublic;
    private UserAllResponse userAllResponse;
    private Integer likes;
    private Integer comments;
    private Integer favorites;
    private boolean pressLike;
    private boolean pressFavorite;
    private List<UserLikePost> usersLikes;
    private boolean edit;

     public PostAllResponse(PostModel post){
        this.id = post.getId();
        this.phrase = post.getPhrase();
        this.img = post.getImg();
        this.datepublic = post.getDatePublic();
        this.userAllResponse = new UserAllResponse(post.getCreator());
        this.likes = post.getLikes() != null ? post.getLikes().size() : 0;
        this.comments = post.getComments() != null ? post.getComments().size() : 0;
        this.favorites = post.getFavorites() != null ? post.getFavorites().size() : 0;
        this.pressLike = false;
        this.pressFavorite = false;
        this.edit = post.isEdit();
    }

    public PostAllResponse(PostModel post, boolean pressLike, boolean pressFavorite, List<UserLikePost> usersLikes){
        this.id = post.getId();
        this.phrase = post.getPhrase();
        this.img = post.getImg();
        this.datepublic = post.getDatePublic();
        this.userAllResponse = new UserAllResponse(post.getCreator());
        this.likes = post.getLikes() != null ? post.getLikes().size() : 0;
        this.comments = post.getComments() != null ? post.getComments().size() : 0;
        this.favorites = post.getFavorites() != null ? post.getFavorites().size() : 0;
        this.pressLike = pressLike;
        this.pressFavorite = pressFavorite;
        this.edit = post.isEdit();
        this.usersLikes = usersLikes;

    }
}
