package com.kristofer.traveling.controllers.user.responses;

import com.kristofer.traveling.models.UserModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class UserAllResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String at;
    private String photo;
    private String banner;

    public UserAllResponse(UserModel userModel){
        this.id = userModel.getId();
        this.firstname = userModel.getFirstname();
        this.lastname = userModel.getLastname();
        this.at = userModel.getAt();
        this.photo = userModel.getPhoto();
        this.banner = userModel.getBanner();
    }

    public UserAllResponse(){

    }
}
