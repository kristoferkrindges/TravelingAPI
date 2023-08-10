package com.kristofer.traveling.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.responses.post.PostAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.LikeModel;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.LikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    public void toggleLike(UserModel user, PostModel post){
        Optional<LikeModel> existingLike = likeRepository.findByUserAndPost(user, post);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        }else{
            var like = LikeModel.builder()
                .user(user)
                .post(post)
                .build();
            likeRepository.save(like);
        }
    }
    
    public List<PostAllResponse> getLikedUserPosts(UserModel user) {
        List<LikeModel> likes = likeRepository.findByUser(user);
        List<PostModel> posts = likes.stream().map(LikeModel::getPost).collect(Collectors.toList());
        List<PostAllResponse> postAllResponse = posts.stream().map(x-> new PostAllResponse(x))
        .collect(Collectors.toList());
        return postAllResponse;
    }

    public List<UserAllResponse> getLikedPostUsers(PostModel post) {
        List<LikeModel> likes = likeRepository.findByPost(post);
        List<UserModel> users = likes.stream().map(LikeModel::getUser).collect(Collectors.toList());
        List<UserAllResponse> usersAllResponse = users.stream().map(x-> new UserAllResponse(x))
        .collect(Collectors.toList());
        return usersAllResponse;
    }
}
