package com.kristofer.traveling.dtos.responses.user;

import java.time.LocalDateTime;

import com.kristofer.traveling.models.UserModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class UserCheckResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String at;
    private String photo;
    private String banner;
    private LocalDateTime birthDate;
    private Integer posts;
    private Integer followers;
    private Integer followings;
    private Integer favorites;
    private Integer likes;

    public UserCheckResponse(UserModel userModel){
        this.id = userModel.getId();
        this.firstname = userModel.getFirstname();
        this.lastname = userModel.getLastname();
        this.email = userModel.getEmail();
        this.at = userModel.getAt();
        this.photo = userModel.getPhoto();
        this.banner = userModel.getBanner();
        this.birthDate = userModel.getBirthdate();
        this.posts = userModel.getPosts().size();
        this.followers = userModel.getFollowers().size();
        this.followings = userModel.getFollowing().size();
        this.favorites = userModel.getFavorites().size();
        this.likes = userModel.getLikes().size();
    }

    public UserCheckResponse(){

    }
}
