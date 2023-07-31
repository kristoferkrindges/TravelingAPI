package com.kristofer.traveling.controllers.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kristofer.traveling.controllers.user.requests.UserUpdateRequest;
import com.kristofer.traveling.controllers.user.responses.UserAllResponse;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.services.UserService;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserAllResponse>>  findAll(){
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserAllResponse> findById(@PathVariable Integer id){
        UserModel user = userService.findById(id);
        return ResponseEntity.ok().body(new UserAllResponse(user));
    }

    @PutMapping()
    public ResponseEntity<UserUpdateRequest> update(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserUpdateRequest request){
        userService.update(authorizationHeader, request);
        return ResponseEntity.ok().body(request);
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") String authorizationHeader){
        userService.delete(authorizationHeader);
        return ResponseEntity.noContent().build();
    }
}
