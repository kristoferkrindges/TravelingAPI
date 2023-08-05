package com.kristofer.traveling.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.FollowerModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.FollowerRepository;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowerService {
    
    private final FollowerRepository followerRepository;

    public List<FollowerModel> findAll(){
        return followerRepository.findAll();
    }

    public List<UserAllResponse> getFollowersUser(Long userId){
        List<UserAllResponse> followers = new ArrayList<>();
        List<FollowerModel> followerRelation = followerRepository.findByFollowingId(userId);
        for(FollowerModel followerRelations : followerRelation){
            followers.add(new UserAllResponse(followerRelations.getFollower()));
        }
        return followers;
    }

    public void insert(UserModel follower, UserModel following){
        this.createdFollowerData(follower, following);
        return;
    }

    public String delete(Long follower, Long following){
        return this.deleteFollowing(follower, following);
    }

    private String deleteFollowing(Long follower, Long following) {
        this.deleteFollower(follower, following);
        return "Delete with sucess!";
    }

    public void deleteFollower(Long follower_id, Long following_id){
        FollowerModel follower = this.findFollower(follower_id, following_id);
        followerRepository.delete(follower);
        return;
    }

    private void createdFollowerData(UserModel follower, UserModel following) {
        var followerRelation = FollowerModel.builder()
            .follower(following)
            .following(follower)
            .build();
        followerRepository.save(followerRelation);
        return;
    }

    private FollowerModel findFollower(Long follower_id, Long following_id) {
        Optional<FollowerModel> follower = followerRepository.findByFollowingAndFollowerId(follower_id, following_id);
        return follower.orElseThrow(() -> new ObjectNotFoundException("Follower not found!"));
    }
}
