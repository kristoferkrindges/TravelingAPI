package com.kristofer.traveling.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.controllers.user.responses.UserAllResponse;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserAllResponse> findAll(){
        List<UserModel> users = userRepository.findAll();
        List<UserAllResponse> userAllResponse = users.stream().map(x-> new UserAllResponse(x))
        .collect(Collectors.toList());
        return userAllResponse;
    }
}
