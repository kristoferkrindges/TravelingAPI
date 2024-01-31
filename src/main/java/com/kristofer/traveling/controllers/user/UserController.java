package com.kristofer.traveling.controllers.user;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<UserAllResponse> findById(@PathVariable UUID id){
        UserModel user = userService.findById(id);
        return ResponseEntity.ok().body(new UserAllResponse(user));
    }

    @GetMapping(value = "/profile/{at}")
    public ResponseEntity<UserAllResponse> findByAt(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PathVariable String at){
        UserAllResponse user = userInteractionService.responseFindByAt(authorizationHeader, userService.findByAt(at));
        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/checkuser")
    public ResponseEntity<UserCheckResponse> checkUser(@RequestHeader("Authorization") String authorizationHeader){
        UserModel user = userService.checkUser(authorizationHeader);
        return ResponseEntity.ok().body(new UserCheckResponse(user));
    }

    @GetMapping(value = "/followersRandom")
    public ResponseEntity<List<UserAllResponse>> findRandomUsersNotFollowing(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(userInteractionService.findRandomUsersNotFollowing(authorizationHeader));
    }

    @PutMapping()
    public ResponseEntity<UserCheckResponse> update(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserUpdateRequest request){
        UserModel user = userService.update(authorizationHeader, request);
        return ResponseEntity.ok().body(new UserCheckResponse(user));
    }

    @PatchMapping(value = "/updatepassword")
    public ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PasswordsRequest obj){
        userService.updatePassword(authorizationHeader, obj);
        return ResponseEntity.ok().body("Password update with success!");
    }

    @PatchMapping(value = "/updatephoto")
    public ResponseEntity<UserCheckResponse> updatePhoto(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PhotoRequest request){
        UserModel user = userService.updatePhoto(authorizationHeader, request);
        return ResponseEntity.ok().body(new UserCheckResponse(user));
    }

    @PatchMapping(value = "/updatebanner")
    public ResponseEntity<UserCheckResponse> updateBanner(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PhotoRequest request){
        UserModel user = userService.updateBanner(authorizationHeader, request);
        return ResponseEntity.ok().body(new UserCheckResponse(user));
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") String authorizationHeader){
        userInteractionService.delete(authorizationHeader);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<String> followUser(@PathVariable("id") UUID followingId, @RequestHeader("Authorization") String authorizationHeader) {
        userInteractionService.processFollow(authorizationHeader, followingId);
        return ResponseEntity.ok("Successfully followed!");
    }

    @GetMapping(value = "/{id}/posts")
    public ResponseEntity<List<PostAllResponse>> findPostsOfUser(@PathVariable String id, @RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(userInteractionService.getPostsOfUser(id, authorizationHeader));
    }

    @GetMapping(value = "/{id}/followings")
    public ResponseEntity<List<UserAllResponse>> findFollowersOfUser(@PathVariable UUID id, @RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(userInteractionService.getFollowingsOfUser(id, authorizationHeader));
    }

    @GetMapping(value = "/{id}/followers")
    public ResponseEntity<List<UserAllResponse>> findFollowingsUser(@PathVariable UUID id, @RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(userInteractionService.getFollowersOfUser(id, authorizationHeader)); 
    }

    @PostMapping("/{id}/removefollow")
    public ResponseEntity<String> removeFollow(@PathVariable("id") UUID followingId, @RequestHeader("Authorization") String authorizationHeader) {
        userInteractionService.removeFollow(authorizationHeader, followingId);
        return ResponseEntity.ok("Successfully remove Follow!");
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<String> toggleLike(@PathVariable("id") UUID followingId, @RequestHeader("Authorization") String authorizationHeader){
        userInteractionService.toggleToLike(authorizationHeader, followingId);
        return ResponseEntity.ok("Successfully like post!");
    }

    @PostMapping("/favorites/{id}")
    public ResponseEntity<String> toggleFavorite(@PathVariable("id") UUID followingId, @RequestHeader("Authorization") String authorizationHeader){
        userInteractionService.toggleFavorite(authorizationHeader, followingId);
        return ResponseEntity.ok("Successfully favorite post!");
    }

    @GetMapping(value = "/likes/{id}")
    public ResponseEntity<List<PostAllResponse>> allLikes(@PathVariable("id") String id, @RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(userInteractionService.allLikesUser(id, authorizationHeader)); 
    }
    @GetMapping(value = "/favorites/{id}")
    public ResponseEntity<List<PostAllResponse>> allFavorites(@PathVariable("id") String id, @RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(userInteractionService.allFavoritesUser(id, authorizationHeader)); 
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
