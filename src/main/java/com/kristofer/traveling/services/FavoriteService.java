package com.kristofer.traveling.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.responses.post.PostAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.FavoriteModel;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.FavoriteRepository;
import com.kristofer.traveling.services.users.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserService userService;
    private final FollowerService followerService;

    public void toggleFavorite(UserModel user, PostModel post){
        Optional<FavoriteModel> existingFavorite = favoriteRepository.findByUserAndPost(user, post);
        if(existingFavorite.isPresent()){
            favoriteRepository.delete(existingFavorite.get());
        }else{
            var favorite = FavoriteModel.builder()
                .user(user)
                .post(post)
                .build();
            favoriteRepository.save(favorite);
        }
    }

    public boolean pressFavorite(UserModel user, PostModel post){
        Optional<FavoriteModel> existingFavorite = favoriteRepository.findByUserAndPost(user, post);
        if (existingFavorite.isPresent()) {
            return true;
        }else{
            return false;
        }
    }

    public List<PostAllResponse> getFavoriteUserPosts(UserModel user){
        // List<FavoriteModel> favorites = favoriteRepository.findByUser(user);
        List<FavoriteModel> favorites = favoriteRepository.findFavoritesByUserOrderByDescendingId(user);
        List<PostModel> posts = favorites.stream().map(FavoriteModel::getPost).collect(Collectors.toList());
        List<PostAllResponse> postAllResponse = posts.stream().map(x-> new PostAllResponse(x))
        .collect(Collectors.toList());
        return postAllResponse;
    }

    public List<UserAllResponse> getFavoritePostUsers(PostModel post, String token){
        List<FavoriteModel> favorites = favoriteRepository.findByPost(post);
        List<UserModel> users = favorites.stream().map(FavoriteModel::getUser).collect(Collectors.toList());
        UserModel userOwner = userService.userByToken(token);
        List<UserAllResponse> usersAllResponse = users.stream().map(
            x-> new UserAllResponse(x, followerService.searchFollower(userOwner, x)))
        .collect(Collectors.toList());
        return usersAllResponse;
    }
}
