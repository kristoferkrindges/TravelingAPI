package com.kristofer.traveling.controllers.user.requests;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String at;
    private String password;
    private String photo;
    private String banner;
    private Date birthdate;
}
