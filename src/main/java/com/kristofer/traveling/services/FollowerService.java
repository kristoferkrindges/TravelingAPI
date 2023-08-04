package com.kristofer.traveling.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.models.FollowerModel;
import com.kristofer.traveling.models.FollowingModel;
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

    public List<UserModel> getFollowersUser(Long userId){
        List<UserModel> followers = new ArrayList<>();
        List<FollowerModel> followerRelation = followerRepository.findByFollowingId(userId);
        // for(FollowerModel followerRelation : followerRelations){

        // }
        return followers;
    }

    public void insert(UserModel follower, UserModel following){
        this.createdFollowerData(follower, following);
        return;
    }

    public void delete(Long follower_id, Long following_id){
        FollowerModel follower = this.findFollower(follower_id, following_id);
        followerRepository.delete(follower);
        return;
    }

    private void createdFollowerData(UserModel follower, UserModel following) {
        var followerRelation = FollowerModel.builder()
            .follower(follower)
            .following(following)
            .build();
        followerRepository.save(followerRelation);
        return;
    }

    private FollowerModel findFollower(Long follower_id, Long following_id) {
        Optional<FollowerModel> follower = followerRepository.findByFollowingAndFollowerId(follower_id, following_id);
        return follower.orElseThrow(() -> new ObjectNotFoundException("Follower not found!"));
    }
}
