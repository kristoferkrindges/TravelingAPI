package com.kristofer.traveling.controllers.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kristofer.traveling.controllers.user.responses.UserAllResponse;
import com.kristofer.traveling.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<UserAllResponse>> findAll(){
        return ResponseEntity.ok().body(userService.findAll());
    }
}
