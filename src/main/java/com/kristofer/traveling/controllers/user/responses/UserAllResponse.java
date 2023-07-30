package com.kristofer.traveling.controllers.user.responses;

import com.kristofer.traveling.models.UserModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor

public class UserAllResponse {
    private String firstname;
    private String lastname;
    private String at;

    public UserAllResponse(UserModel userModel){
        this.firstname = userModel.getFirstname();
        this.lastname = userModel.getLastname();
        this.at = userModel.getAt();
    }

    public UserAllResponse(){

    }
}
