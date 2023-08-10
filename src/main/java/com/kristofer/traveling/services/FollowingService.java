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

    public String delete(UserModel follower, UserModel following){
        return this.deleteBothTables(follower, following);
    }
    
    private String deleteBothTables(UserModel follower, UserModel following) {
        this.deleteFollowing(follower, following);
        return "Deleted with sucess!";

    }

    public void deleteFollowing(UserModel follower, UserModel following){
        FollowingModel followingModel = this.findFollowing(follower, following);
        followingRepository.delete(followingModel);
        return;
    }

    private FollowingModel findFollowing(UserModel follower, UserModel following) {
        Optional<FollowingModel> followingModel = followingRepository.findByFollowerAndFollowing(following, follower);
        return followingModel.orElseThrow(() -> new ObjectNotFoundException("Follower not found!"));
        
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
