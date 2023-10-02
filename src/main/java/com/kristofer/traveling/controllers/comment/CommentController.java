package com.kristofer.traveling.controllers.comment;

import java.util.List;

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
import com.kristofer.traveling.services.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping()
    public ResponseEntity<List<CommentAllResponse>> findAll(){
        return ResponseEntity.ok().body(commentService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommentAllResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(commentService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<CommentAllResponse> insert(@RequestHeader("Authorization") String authorizationHeader, @RequestBody CommentRequest request){
        return ResponseEntity.ok().body(commentService.insert(authorizationHeader, request));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CommentAllResponse> update(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id ,@RequestBody CommentRequest request){
        return ResponseEntity.ok().body(commentService.update(authorizationHeader, request, id));
    }
    
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        return ResponseEntity.ok().body(commentService.delete(authorizationHeader, id));
    }
    @GetMapping(value = "/postComments/{id}")
    public ResponseEntity<List<CommentAllResponse>> postComments(@PathVariable("id") Long postId){
        return ResponseEntity.ok().body(commentService.getPostComments(postId)); 
    }
    @GetMapping(value = "/childComment/{id}")
    public ResponseEntity<List<CommentAllResponse>> childComment(@PathVariable("id") Long postId){
        return ResponseEntity.ok().body(commentService.getChildComment(postId)); 
    }
    @PostMapping("/like/{id}")
    public ResponseEntity<String> toggleLike(@PathVariable("id") Long commentId, @RequestHeader("Authorization") String authorizationHeader){
        commentService.toogleLikeComment(authorizationHeader, commentId);
        return ResponseEntity.ok("Successfully like comment!");
    }
    @GetMapping(value = "/like/{id}")
    public ResponseEntity<List<UserAllResponse>> allLikes(@PathVariable("id") Long commentId, @RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(commentService.getLikedCommentsUser(commentId, authorizationHeader)); 
    }
}
