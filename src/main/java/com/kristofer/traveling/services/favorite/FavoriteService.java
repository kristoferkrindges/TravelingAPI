package com.kristofer.traveling.services.favorite;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.FavoriteModel;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.FavoriteRepository;
import com.kristofer.traveling.services.follow.FollowerService;
import com.kristofer.traveling.services.users.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserService userService;
    private final FollowerService followerService;

    public boolean toggleFavorite(UserModel user, PostModel post){
        Optional<FavoriteModel> existingFavorite = favoriteRepository.findByUserAndPost(user, post);
        if(existingFavorite.isPresent()){
            favoriteRepository.delete(existingFavorite.get());
            return false;
        }else{
            var favorite = FavoriteModel.builder()
                .user(user)
                .post(post)
                .build();
            favoriteRepository.save(favorite);
            return true;
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

    public List<FavoriteModel> getFavoriteUserPosts(UserModel user){
        List<FavoriteModel> favorites = favoriteRepository.findFavoritesByUserOrderByDescendingId(user);
        return favorites;
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

    public void deleteAllFavoritesPosts(PostModel postModel) {
        List<FavoriteModel> favorites = favoriteRepository.findByPost(postModel);
        favoriteRepository.deleteAll(favorites);
    }

    public void deleteAllFavoritesUser(UserModel userModel) {
        List<FavoriteModel> favorites = favoriteRepository.findByUser(userModel);
        favoriteRepository.deleteAll(favorites);
    }
}
