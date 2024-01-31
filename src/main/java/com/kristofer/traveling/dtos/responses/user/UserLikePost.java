package com.kristofer.traveling.dtos.responses.user;

import java.util.UUID;

import com.kristofer.traveling.models.UserModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserLikePost {
    private UUID id;
    private String firstname;
    private String lastname;
    private String at;
    private String photo;

    public UserLikePost(UserModel userModel){
        this.id = userModel.getId();
        this.firstname = userModel.getFirstname();
        this.lastname = userModel.getLastname();
        this.at = userModel.getAt();
        this.photo = userModel.getPhoto();
    }


    public UserLikePost(){

    }
}
