package com.kristofer.traveling.controllers.storie;

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

import com.kristofer.traveling.dtos.requests.storie.StorieRequest;
import com.kristofer.traveling.dtos.responses.storie.StorieAllResponse;
import com.kristofer.traveling.services.StorieService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StorieController {
    private final StorieService storieService;

    @GetMapping()
    public ResponseEntity<List<StorieAllResponse>> findAll(){
        return ResponseEntity.ok().body(storieService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<StorieAllResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(storieService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<StorieAllResponse> insert(@RequestHeader("Authorization") String authorizationHeader, @RequestBody StorieRequest request){
        return ResponseEntity.ok().body(storieService.insert(authorizationHeader, request));
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<StorieAllResponse> update(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id ,@RequestBody StorieRequest request){
        return ResponseEntity.ok().body(storieService.update(authorizationHeader, request, id));
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        return ResponseEntity.ok().body(storieService.delete(authorizationHeader, id));
    }
}