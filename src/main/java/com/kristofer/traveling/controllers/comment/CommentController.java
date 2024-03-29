package com.kristofer.traveling.controllers.comment;

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

import com.kristofer.traveling.dtos.requests.comment.CommentRequest;
import com.kristofer.traveling.dtos.responses.comment.CommentAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.services.comment.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping()
    public ResponseEntity<List<CommentAllResponse>> findAll(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(commentService.findAll(authorizationHeader));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommentAllResponse> findById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable UUID id){
        return ResponseEntity.ok().body(commentService.findById(authorizationHeader, id));
    }

    @PostMapping()
    public ResponseEntity<CommentAllResponse> insert(@RequestHeader("Authorization") String authorizationHeader, @RequestBody CommentRequest request){
        return ResponseEntity.ok().body(commentService.insert(authorizationHeader, request));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CommentAllResponse> update(@RequestHeader("Authorization") String authorizationHeader, @PathVariable UUID id ,@RequestBody CommentRequest request){
        return ResponseEntity.ok().body(commentService.update(authorizationHeader, request, id));
    }
    
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String authorizationHeader, @PathVariable UUID id){
        return ResponseEntity.ok().body(commentService.delete(authorizationHeader, id));
    }
    @GetMapping(value = "/postComments/{id}")
    public ResponseEntity<List<CommentAllResponse>> postComments(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") UUID postId){
        return ResponseEntity.ok().body(commentService.getPostComments(authorizationHeader, postId)); 
    }
    @GetMapping(value = "/childComment/{id}")
    public ResponseEntity<List<CommentAllResponse>> childComment(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") UUID postId){
        return ResponseEntity.ok().body(commentService.getChildComment(authorizationHeader, postId)); 
    }
    @PostMapping("/like/{id}")
    public ResponseEntity<String> toggleLike(@PathVariable("id") UUID commentId, @RequestHeader("Authorization") String authorizationHeader){
        commentService.toogleLikeComment(authorizationHeader, commentId);
        return ResponseEntity.ok("Successfully like comment!");
    }
    @GetMapping(value = "/like/{id}")
    public ResponseEntity<List<UserAllResponse>> allLikes(@PathVariable("id") UUID commentId, @RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(commentService.getLikedCommentsUser(commentId, authorizationHeader)); 
    }
}
