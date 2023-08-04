package com.kristofer.traveling.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.models.FollowingModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.FollowingRepository;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowingService {
    private final FollowingRepository followingRepository;
    private final UserService userService;
    private final FollowerService followerService;

    public List<FollowingModel> findAll(){
        return followingRepository.findAll();
    }

    public List<UserModel> getFollowingsUser(){
        List<UserModel> followings = new ArrayList<>();
        return followings;
    }

    public FollowingModel insert(String token, Long following_id){
        return this.createdFollowingData(token, following_id);
    }

    public String delete(String token, Long following_id){
        return this.deleteBothTables(token, following_id);
    }
    
    private String deleteBothTables(String token, Long following_id) {
        UserModel followerUser = userService.userByToken(token);
        FollowingModel following = this.findFollowing(followerUser.getId(), following_id);
        followingRepository.delete(following);
        this.followerService.delete(followerUser.getId(), following_id);
        return "Deleted with sucess!";

    }

    private FollowingModel findFollowing(Long follower_id, Long following_id) {
        Optional<FollowingModel> following = followingRepository.findByFollowingAndFollowerId(following_id, follower_id);
        return following.orElseThrow(() -> new ObjectNotFoundException("Following not found!"));
        
    }

    private FollowingModel createdFollowingData(String token, Long following_id) {
        UserModel follower = userService.userByToken(token);
        UserModel following = userService.findById(following_id);
        var followingRelation = FollowingModel.builder()
            .follower(follower)
            .following(following)
            .build();
        followingRepository.save(followingRelation);
        followerService.insert(follower, following);
        return followingRelation;
    }
}
