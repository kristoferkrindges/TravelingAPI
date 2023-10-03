package com.kristofer.traveling.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.responses.post.PostAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.CommentModel;
import com.kristofer.traveling.models.LikeModel;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.LikeRepository;
import com.kristofer.traveling.services.users.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final FollowerService followerService;
    private final UserService userService;

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

    public boolean pressLike(UserModel user, PostModel post){
        Optional<LikeModel> existingLike = likeRepository.findByUserAndPost(user, post);
        if (existingLike.isPresent()) {
            return true;
        }else{
            return false;
        }
    }

    public void toggleLikeComment(UserModel user, CommentModel comment){
        Optional<LikeModel> existingLike = likeRepository.findByUserAndComment(user, comment);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        }else{
            var like = LikeModel.builder()
                .user(user)
                .comment(comment)
                .build();
            likeRepository.save(like);
        }
    }
    
    public List<PostAllResponse> getLikedUserPosts(UserModel user) {
        // List<LikeModel> likes = likeRepository.findByUser(user);
        List<LikeModel> likes = likeRepository.findLikesByUserOrderByDescendingId(user);
        List<PostModel> posts = likes.stream().map(LikeModel::getPost).collect(Collectors.toList());
        List<PostAllResponse> postAllResponse = posts.stream().map(x-> new PostAllResponse(x))
        .collect(Collectors.toList());
        return postAllResponse;
    }

    public List<UserAllResponse> getLikedPostUsers(PostModel post, String token) {
        List<LikeModel> likes = likeRepository.findByPost(post);
        List<UserModel> users = likes.stream().map(LikeModel::getUser).collect(Collectors.toList());
        UserModel userOwner = userService.userByToken(token);
        List<UserAllResponse> usersAllResponse = users.stream().map(x-> new UserAllResponse(x, followerService.searchFollower(userOwner, x)))
        .collect(Collectors.toList());
        return usersAllResponse;
    }

    public List<UserAllResponse> getLikedCommentsUser(CommentModel comment, String token) {
        List<LikeModel> likes = likeRepository.findByComment(comment);
        List<UserModel> users = likes.stream().map(LikeModel::getUser).collect(Collectors.toList());
        UserModel userOwner = userService.userByToken(token);
        List<UserAllResponse> usersAllResponse = users.stream().map(x-> new UserAllResponse(x, followerService.searchFollower(userOwner, x)))
        .collect(Collectors.toList());
        return usersAllResponse;
    }
}
