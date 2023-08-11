package com.kristofer.traveling.services.users;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.responses.post.PostAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.FollowingModel;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.models.Enums.NotificationTypeEnum;
import com.kristofer.traveling.services.FavoriteService;
import com.kristofer.traveling.services.FollowerService;
import com.kristofer.traveling.services.FollowingService;
import com.kristofer.traveling.services.LikeService;
import com.kristofer.traveling.services.NotificationService;
import com.kristofer.traveling.services.PostService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInteractionService {
    private final FollowerService followerService;
    private final FollowingService followingService;
    private final UserService userService;
    private final PostService postService;
    private final LikeService likeService;
    private final FavoriteService favoriteService;
    private final NotificationService notificationService;

    public FollowingModel followUser(String token, Long followingId) {
        UserModel follower = userService.userByToken(token);
        UserModel following = userService.findById(followingId);

        followerService.insert(follower, following);
        notificationService.createNotification(following, NotificationTypeEnum.FOLLOW, follower, null);
        return followingService.insert(follower, following);
    }

    public void unfollowUser(String token, Long followingId) {
        UserModel follower = userService.userByToken(token);
        UserModel following = userService.findById(followingId);

        followerService.delete(follower, following);
        followingService.delete(follower, following);
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
        followerService.delete(follower, following);
        followingService.delete(follower, following);
    }

    public void toggleToLike(String token, Long postId){
        UserModel user = userService.userByToken(token);
        PostModel post = postService.findByIdPost(postId);
        likeService.toggleLike(user, post);
        notificationService.createNotification(post.getCreator(), NotificationTypeEnum.LIKEPOST, user, postId);
    }

    public void toggleFavorite(String token, Long postId){
        UserModel user = userService.userByToken(token);
        PostModel post = postService.findByIdPost(postId);
        favoriteService.toggleFavorite(user, post);
        notificationService.createNotification(post.getCreator(), NotificationTypeEnum.FAVORITEPOST, user, postId);
    }

    public List<PostAllResponse> allLikesUser(String token){
        UserModel user = userService.userByToken(token);
        return likeService.getLikedUserPosts(user);
    }

    public List<PostAllResponse> allFavoritesUser(String token){
        UserModel user = userService.userByToken(token);
        return favoriteService.getFavoriteUserPosts(user);
    }
    
}
