package com.kristofer.traveling.controllers.user.requests;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordsRequest {
    private String password;
    private String newpassword;
}
