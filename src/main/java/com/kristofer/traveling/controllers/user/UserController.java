package com.kristofer.traveling.controllers.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kristofer.traveling.controllers.user.requests.PasswordsRequest;
import com.kristofer.traveling.controllers.user.requests.UserUpdateRequest;
import com.kristofer.traveling.controllers.user.responses.UserAllResponse;
import com.kristofer.traveling.controllers.user.responses.UserCheckResponse;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserAllResponse>> findAll(){
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserAllResponse> findById(@PathVariable Integer id){
        UserModel user = userService.findById(id);
        return ResponseEntity.ok().body(new UserAllResponse(user));
    }

    @GetMapping(value = "/checkuser")
    public ResponseEntity<UserCheckResponse> checkUser(@RequestHeader("Authorization") String authorizationHeader){
        UserModel user = userService.checkUser(authorizationHeader);
        return ResponseEntity.ok().body(new UserCheckResponse(user));
    }

    @PutMapping()
    public ResponseEntity<UserUpdateRequest> update(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserUpdateRequest request){
        userService.update(authorizationHeader, request);
        return ResponseEntity.ok().body(request);
    }

    @PatchMapping(value = "/updatepassword")
    public ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PasswordsRequest obj){
        userService.updatePassword(authorizationHeader, obj);
        return ResponseEntity.ok().body("Password update with sucess!");
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") String authorizationHeader){
        userService.delete(authorizationHeader);
        return ResponseEntity.noContent().build();
    }
}
