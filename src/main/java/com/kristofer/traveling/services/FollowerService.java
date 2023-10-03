package com.kristofer.traveling.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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

    public List<UserModel> getFollowersUser(Long userId){
        List<UserModel> followers = new ArrayList<>();
        List<FollowerModel> followerRelation = followerRepository.findByFollowingIdOrderByidDesc(userId);
        for(FollowerModel followerRelations : followerRelation){
            followers.add(followerRelations.getFollower());
        }
        return followers;
    }

    public void insert(UserModel follower, UserModel following){
        this.createdFollowerData(follower, following);
        return;
    }

    public String delete(UserModel follower, UserModel following){
        return this.deleteFollowing(follower, following);
    }

    private String deleteFollowing(UserModel follower, UserModel following) {
        this.deleteFollower(follower, following);
        return "Delete with sucess!";
    }

    public void deleteFollower(UserModel follower, UserModel following){
        FollowerModel followerModel = this.findFollower(follower, following);
        followerRepository.delete(followerModel);
        return;
    }

    public boolean FollowOrDelete(UserModel follower, UserModel following){
        Optional<FollowerModel> followerModel = followerRepository.findByFollowerAndFollowing(following, follower);
        if(followerModel.isPresent()){
            followerRepository.delete(followerModel.get());
            return false;
        }else{
            this.insert(follower, following);
            return true;
        }
        
    }

    private void createdFollowerData(UserModel follower, UserModel following) {
        var followerRelation = FollowerModel.builder()
            .follower(following)
            .following(follower)
            .build();
        followerRepository.save(followerRelation);
        return;
    }

    public boolean searchFollower(UserModel follower, UserModel following){
        Optional<FollowerModel> followerModel = followerRepository.findByFollowerAndFollowing(following, follower);
        if(followerModel.isPresent()){
            return true;
        }else{
            return false;
        }
    }

    private FollowerModel findFollower(UserModel follower, UserModel following) {
        Optional<FollowerModel> followerModel = followerRepository.findByFollowerAndFollowing(following, follower);
        return followerModel.orElseThrow(() -> new ObjectNotFoundException("Follower not found!"));
    }
}
