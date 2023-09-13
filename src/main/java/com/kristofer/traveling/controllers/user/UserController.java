package com.kristofer.traveling.controllers.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kristofer.traveling.dtos.requests.user.PasswordsRequest;
import com.kristofer.traveling.dtos.requests.user.PhotoRequest;
import com.kristofer.traveling.dtos.requests.user.UserUpdateRequest;
import com.kristofer.traveling.dtos.responses.post.PostAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserCheckResponse;
import com.kristofer.traveling.models.ConfigurationModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.services.users.UserInteractionService;
import com.kristofer.traveling.services.users.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserInteractionService userInteractionService;

    @GetMapping()
    public ResponseEntity<List<UserAllResponse>> findAll(){
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserAllResponse> findById(@PathVariable Long id){
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
        return ResponseEntity.ok().body("Password update with success!");
    }

    @PatchMapping(value = "/updatephoto")
    public ResponseEntity<String> updatePhoto(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PhotoRequest request){
        userService.updatePhoto(authorizationHeader, request);
        return ResponseEntity.ok().body("Photo update with success!");
    }

    @PatchMapping(value = "/updatebanner")
    public ResponseEntity<String> updateBanner(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PhotoRequest request){
        userService.updateBanner(authorizationHeader, request);
        return ResponseEntity.ok().body("Banner update with success!");
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") String authorizationHeader){
        userService.delete(authorizationHeader);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<String> followUser(@PathVariable("id") Long followingId, @RequestHeader("Authorization") String authorizationHeader) {
        userInteractionService.followUser(authorizationHeader, followingId);
        return ResponseEntity.ok("Successfully followed!");
    }

    @PostMapping("/{id}/unfollow")
    public ResponseEntity<String> unfollowUser(@PathVariable("id") Long followingId, @RequestHeader("Authorization") String authorizationHeader) {
        userInteractionService.unfollowUser(authorizationHeader, followingId);
        return ResponseEntity.ok("Successfully unfollowed!");
    }

    @GetMapping(value = "/{id}/followings")
    public ResponseEntity<List<UserAllResponse>> findFollowersOfUser(@PathVariable Long id){
        return ResponseEntity.ok().body(userInteractionService.getFollowingsOfUser(id));
    }

    @GetMapping(value = "/{id}/followers")
    public ResponseEntity<List<UserAllResponse>> findFollowingsUser(@PathVariable Long id){
        return ResponseEntity.ok().body(userInteractionService.getFollowersOfUser(id)); 
    }

    @PostMapping("/{id}/removefollow")
    public ResponseEntity<String> removeFollow(@PathVariable("id") Long followingId, @RequestHeader("Authorization") String authorizationHeader) {
        userInteractionService.removeFollow(authorizationHeader, followingId);
        return ResponseEntity.ok("Successfully remove Follow!");
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<String> toggleLike(@PathVariable("id") Long followingId, @RequestHeader("Authorization") String authorizationHeader){
        userInteractionService.toggleToLike(authorizationHeader, followingId);
        return ResponseEntity.ok("Successfully like post!");
    }

    @PostMapping("/favorites/{id}")
    public ResponseEntity<String> toggleFavorite(@PathVariable("id") Long followingId, @RequestHeader("Authorization") String authorizationHeader){
        userInteractionService.toggleFavorite(authorizationHeader, followingId);
        return ResponseEntity.ok("Successfully favorite post!");
    }

    @GetMapping(value = "/like")
    public ResponseEntity<List<PostAllResponse>> allLikes(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(userInteractionService.allLikesUser(authorizationHeader)); 
    }
    @GetMapping(value = "/favorites")
    public ResponseEntity<List<PostAllResponse>> allFavorites(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(userInteractionService.allFavoritesUser(authorizationHeader)); 
    }

    @GetMapping(value = "/configuration")
    public ResponseEntity<ConfigurationModel> configurationUser(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(userInteractionService.getAllConfigurationUser(authorizationHeader)); 
    }
    @PostMapping("/darkmode")
    public ResponseEntity<Void> toggleDarkMode(@RequestHeader("Authorization") String authorizationHeader){
        userInteractionService.toggleDarkMode(authorizationHeader);
        return ResponseEntity.noContent().build();
    }
}
