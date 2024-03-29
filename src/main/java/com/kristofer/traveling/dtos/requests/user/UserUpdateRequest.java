package com.kristofer.traveling.dtos.requests.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private String firstname;
    private String lastname;
    private String at;
    private String birthDate;
}
