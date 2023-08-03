package com.kristofer.traveling.dtos.responses.user;

import java.util.Date;

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
    private Date birthdate;
    private Integer posts;
    private Integer followers;
    private Integer followings;

    public UserCheckResponse(UserModel userModel){
        this.id = userModel.getId();
        this.firstname = userModel.getFirstname();
        this.lastname = userModel.getLastname();
        this.email = userModel.getEmail();
        this.at = userModel.getAt();
        this.photo = userModel.getPhoto();
        this.banner = userModel.getBanner();
        this.birthdate = userModel.getBirthdate();
        this.posts = userModel.getPosts().size();
        this.followers = userModel.getFollowers().size();
        this.followings = userModel.getFollowing().size();
    }

    public UserCheckResponse(){

    }
}
