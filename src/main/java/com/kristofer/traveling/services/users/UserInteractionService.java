package com.kristofer.traveling.services.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.responses.post.PostAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.ConfigurationModel;
import com.kristofer.traveling.models.FavoriteModel;
import com.kristofer.traveling.models.LikeModel;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.models.Enums.NotificationTypeEnum;
import com.kristofer.traveling.repositories.UserRepository;
import com.kristofer.traveling.services.configuration.ConfigurationService;
import com.kristofer.traveling.services.event.EventService;
import com.kristofer.traveling.services.exceptions.DatabaseException;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;
import com.kristofer.traveling.services.favorite.FavoriteService;
import com.kristofer.traveling.services.follow.FollowerService;
import com.kristofer.traveling.services.follow.FollowingService;
import com.kristofer.traveling.services.like.LikeService;
import com.kristofer.traveling.services.notification.NotificationService;
import com.kristofer.traveling.services.post.PostService;

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
    private final EventService eventService;

    private final UserRepository userRepository;

    public String processFollow(String token, UUID followId){
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

    public List<UserAllResponse> getFollowingsOfUser(UUID id, String token) {
        UserModel owner = userService.userByToken(token);
        List<UserAllResponse> followers = new ArrayList<>();

        for(UserModel user : followerService.getFollowersUser(id)){
            followers.add(new UserAllResponse(user, followerService.searchFollower(owner, user)));
        }
        return followers;
    }

    public List<UserAllResponse> getFollowersOfUser(UUID id, String token) {
        UserModel owner = userService.userByToken(token);
        List<UserAllResponse> followings = new ArrayList<>();
        for(UserModel user : followingService.getFollowingsUser(id)){
            followings.add(new UserAllResponse(user, followingService.searchFollowing(owner, user)));
        }
        return followings;
    }

    public void removeFollow(String token, UUID followingId){
        UserModel follower = userService.userByToken(token);
        UserModel following = userService.findById(followingId);
        followerService.delete(follower, following);
        followingService.delete(follower, following);
    }

    public void toggleToLike(String token, UUID postId){
        UserModel user = userService.userByToken(token);
        PostModel post = postService.findByIdPost(postId);
        Boolean verify = likeService.toggleLike(user, post);
        if(verify){
            notificationService.createNotification(post.getCreator(), NotificationTypeEnum.LIKEPOST, user, postId);
        }
    }

    public void toggleFavorite(String token, UUID postId){
        UserModel user = userService.userByToken(token);
        PostModel post = postService.findByIdPost(postId);
        Boolean verify = favoriteService.toggleFavorite(user, post);
        if(verify){
            notificationService.createNotification(post.getCreator(), NotificationTypeEnum.FAVORITEPOST, user, postId);
        }
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

    public List<UserAllResponse> findRandomUsersNotFollowing(String token) {
        UserModel userToken = userService.userByToken(token);
        List<UserModel> notFollowingUsers = userService.findAllUserModel().stream()
                .filter(user -> user.getId() != userToken.getId() && !followerService.searchFollower(userToken, user))
                .collect(Collectors.toList());
    
        if (notFollowingUsers.size() < 1) {
            return List.of();
        }
    
        List<UserAllResponse> userAllResponse = notFollowingUsers.stream()
                .map(UserAllResponse::new)
                .collect(Collectors.toList());
    
        Random random = new Random();
        int numberOfRandomUsers = Math.min(2, notFollowingUsers.size());
        
        List<UserAllResponse> randomUsers = random.ints(0, userAllResponse.size())
                .distinct()
                .limit(numberOfRandomUsers)
                .mapToObj(userAllResponse::get)
                .collect(Collectors.toList());
    
        return randomUsers;
    }

    public void delete(String token){
        UserModel user = userService.userByToken(token);
        followingService.removeAllFollowingsForUser(user);
        followerService.removeAllFollowersForUser(user);
        notificationService.deletAllNotificationsByUser(user);
        likeService.deleteAllLikesUser(user);
        favoriteService.deleteAllFavoritesUser(user);
        eventService.deleteAllEventsUser(user);
        configurationService.deletConfigurationUser(user.getId());
        try {
            userRepository.deleteById(user.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("User with id " + user.getId() + " not found");
        }catch(DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }

}
