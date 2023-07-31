package com.kristofer.traveling.controllers.user.responses;

import com.kristofer.traveling.models.UserModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserCheckResponse {
    private String firstname;
    private String lastname;
    private String email;
    private String at;

    public UserCheckResponse(UserModel userModel){
        this.firstname = userModel.getFirstname();
        this.lastname = userModel.getLastname();
        this.email = userModel.getEmail();
        this.at = userModel.getAt();
    }

    public UserCheckResponse(){

    }
}
