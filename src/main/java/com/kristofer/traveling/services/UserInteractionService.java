package com.kristofer.traveling.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.FollowingModel;
import com.kristofer.traveling.models.UserModel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInteractionService {
    private final FollowerService followerService;
    private final FollowingService followingService;
    private final UserService userService;

    public FollowingModel followUser(String token, Long followingId) {
        UserModel follower = userService.userByToken(token);
        UserModel following = userService.findById(followingId);

        followerService.insert(follower, following);
        return followingService.insert(follower, following);
    }

    public void unfollowUser(String token, Long followingId) {
        UserModel follower = userService.userByToken(token);
        UserModel following = userService.findById(followingId);

        followerService.delete(follower.getId(), followingId);
        followingService.delete(follower.getId(), following.getId());
    }

    public List<UserAllResponse> getFollowingsOfUser(Long id) {
        return followerService.getFollowersUser(id);
    }
    public List<UserAllResponse> getFollowersOfUser(Long id) {
        return followingService.getFollowingsUser(id);
    }

    public void removeFollow(String token, Long followingId){
        UserModel follower = userService.userByToken(token);
        UserModel following = userService.findById(followingId);
        followerService.delete(follower.getId(), followingId);
        followingService.delete(follower.getId(), following.getId());
    }

    // Favs methos;
}
