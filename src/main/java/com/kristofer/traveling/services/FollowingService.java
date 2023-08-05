package com.kristofer.traveling.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.FollowingModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.FollowingRepository;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowingService {
    private final FollowingRepository followingRepository;

    public List<FollowingModel> findAll(){
        return followingRepository.findAll();
    }

    public List<UserAllResponse> getFollowingsUser(Long userId){
        List<UserAllResponse> followings = new ArrayList<>();
        List<FollowingModel> followingRelation = followingRepository.findByFollowerId(userId);
        for(FollowingModel followingRelations : followingRelation){
            followings.add(new UserAllResponse(followingRelations.getFollowing()));
        }
        return followings;
    }

    public FollowingModel insert(UserModel follower, UserModel following){
        return this.createdFollowingData(follower, following);
    }

    public String delete(Long follower, Long following){
        return this.deleteBothTables(follower, following);
    }
    
    private String deleteBothTables(Long follower, Long following) {
        this.deleteFollowing(follower, following);
        return "Deleted with sucess!";

    }

    public void deleteFollowing(Long follower_id, Long following_id){
        FollowingModel following = this.findFollowing(follower_id, following_id);
        followingRepository.delete(following);
        return;
    }

    private FollowingModel findFollowing(Long follower_id, Long following_id) {
        Optional<FollowingModel> following = followingRepository.findByFollowingAndFollowerId(following_id, follower_id);
        return following.orElseThrow(() -> new ObjectNotFoundException("Following not found!"));
        
    }

    private FollowingModel createdFollowingData(UserModel follower, UserModel following) {
        var followingRelation = FollowingModel.builder()
            .follower(following)
            .following(follower)
            .build();
        followingRepository.save(followingRelation);
        return followingRelation;
    }
}
