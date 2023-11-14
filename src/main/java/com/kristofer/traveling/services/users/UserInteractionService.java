package com.kristofer.traveling.services.users;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.responses.post.PostAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.ConfigurationModel;
import com.kristofer.traveling.models.FavoriteModel;
import com.kristofer.traveling.models.FollowerModel;
import com.kristofer.traveling.models.LikeModel;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.models.Enums.NotificationTypeEnum;
import com.kristofer.traveling.services.ConfigurationService;
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
    private final ConfigurationService configurationService;

    public String processFollow(String token, Long followId){
        UserModel owner = userService.userByToken(token);
        UserModel following = userService.findById(followId);
        if(followerService.FollowOrDelete(owner, following)){
            notificationService.createNotification(following, NotificationTypeEnum.FOLLOW, owner, null);
            followingService.insert(owner, following);
            return "Follow user with success!";
        }else{
            followingService.delete(owner, following);
            return "Unfollow user with success!";
        }
    }

    public UserAllResponse responseFindByAt(String token, UserModel user){
        UserModel owner;
        UserAllResponse userAt = new UserAllResponse(user);
        if(token != null && !token.isEmpty()){
            owner = userService.userByToken(token);
            userAt.setFollow(followerService.searchFollower(owner, user));
        }else{
            userAt.setFollow(false);
        }
        return userAt;
    }

    public List<UserAllResponse> getFollowingsOfUser(Long id, String token) {
        UserModel owner = userService.userByToken(token);
        List<UserAllResponse> followers = new ArrayList<>();
        for(UserModel user : followerService.getFollowersUser(id)){
            followers.add(new UserAllResponse(user, followerService.searchFollower(owner, user)));
        }
        return followers;
    }

    // Modify
    public List<UserAllResponse> getFollowersOfUser(Long id, String token) {
        UserModel owner = userService.userByToken(token);
        List<UserAllResponse> followings = new ArrayList<>();
        for(UserModel user : followingService.getFollowingsUser(id)){
            followings.add(new UserAllResponse(user, followingService.searchFollowing(owner, user)));
        }
        return followings;
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

    public void toggleDarkMode(String token){
        UserModel user = userService.userByToken(token);
        configurationService.toogleDarkMode(user.getId());
    }

    public List<PostAllResponse> getPostsOfUser(String at, String token) {
        UserModel user = userService.findByAt(at);
        return postService.findByUserOwner(user.getId(), token);
    }

    public List<PostAllResponse> allLikesUser(String id, String token){
        UserModel user = userService.findByAt(id);
        List<LikeModel> likes = likeService.getLikedUserPosts(user);
        List<PostModel> posts = likes.stream().map(LikeModel::getPost).collect(Collectors.toList());
        List<PostAllResponse> postAllResponse = posts.stream().map(x-> new PostAllResponse(
            x, postService.pressLike(token, x), postService.pressFavorite(token, x), likeService.findTop3UsersWhoLikedPost(x.getId())))
            .collect(Collectors.toList());
        return postAllResponse;
    }

    public List<PostAllResponse> allFavoritesUser(String id, String token){
        UserModel user = userService.findByAt(id);
        List<FavoriteModel> favorites = favoriteService.getFavoriteUserPosts(user);
        List<PostModel> posts = favorites.stream().map(FavoriteModel::getPost).collect(Collectors.toList());
        List<PostAllResponse> postAllResponse = posts.stream().map(x-> new PostAllResponse(
            x, postService.pressLike(token, x), postService.pressFavorite(token, x), likeService.findTop3UsersWhoLikedPost(x.getId())))
            .collect(Collectors.toList());
        return postAllResponse;
    }

    public ConfigurationModel getAllConfigurationUser(String token){
        UserModel user = userService.userByToken(token);
        return configurationService.getAllConfigurationsUser(user.getId());
    }

    
}
