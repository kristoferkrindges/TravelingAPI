package com.kristofer.traveling.controllers.post;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kristofer.traveling.dtos.requests.post.PostRequest;
import com.kristofer.traveling.dtos.responses.post.PostAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.services.post.PostInteractionService;
import com.kristofer.traveling.services.post.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostInteractionService postInteractionService;

    @GetMapping()
    public ResponseEntity<List<PostAllResponse>> findAll(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(postService.findAll(authorizationHeader));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostAllResponse> findById(@PathVariable UUID id, @RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(postService.findById(id, authorizationHeader));
    }

    @PostMapping()
    public ResponseEntity<PostAllResponse> insert(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PostRequest request){
        return ResponseEntity.ok().body(postService.insert(authorizationHeader, request));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PostAllResponse> update(@RequestHeader("Authorization") String authorizationHeader, @PathVariable UUID id ,@RequestBody PostRequest request){
        return ResponseEntity.ok().body(postService.update(authorizationHeader, request, id));
    }
    
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String authorizationHeader, @PathVariable UUID id){
        return ResponseEntity.ok().body(postInteractionService.delete(authorizationHeader, id));
    }
    @GetMapping(value = "/likes/{id}")
    public ResponseEntity<List<UserAllResponse>> allLikes(@PathVariable("id") UUID postId, @RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(postService.allLikesPost(postId, authorizationHeader)); 
    }
    @GetMapping(value = "/favorites/{id}")
    public ResponseEntity<List<UserAllResponse>> allFavorites(@PathVariable("id") UUID postId, @RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(postService.allFavoritesPost(postId, authorizationHeader)); 
    }
}
