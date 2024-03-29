package com.kristofer.traveling.services.like;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserLikePost;
import com.kristofer.traveling.models.CommentModel;
import com.kristofer.traveling.models.LikeModel;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.StorieModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.LikeRepository;
import com.kristofer.traveling.services.follow.FollowerService;
import com.kristofer.traveling.services.users.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final FollowerService followerService;
    private final UserService userService;

    public boolean toggleLike(UserModel user, PostModel post){
        Optional<LikeModel> existingLike = likeRepository.findByUserAndPost(user, post);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return false;
        }else{
            var like = LikeModel.builder()
                .user(user)
                .post(post)
                .build();
            likeRepository.save(like);
            return true;
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

    public boolean pressLikeStorie(UserModel user, StorieModel storie){
        Optional<LikeModel> existingLike = likeRepository.findByUserAndStorie(user, storie);
        if (existingLike.isPresent()) {
            return true;
        }else{
            return false;
        }
    }

    public boolean pressLikeComment(UserModel user, CommentModel comment) {
        Optional<LikeModel> existingLike = likeRepository.findByUserAndComment(user, comment);
        if (existingLike.isPresent()) {
            return true;
        }else{
            return false;
        }
    }

    public boolean toggleLikeComment(UserModel user, CommentModel comment){
        Optional<LikeModel> existingLike = likeRepository.findByUserAndComment(user, comment);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return false;
        }else{
            var like = LikeModel.builder()
                .user(user)
                .comment(comment)
                .build();
            likeRepository.save(like);
            return true;
        }
    }

    public boolean toggleLikeStorie(UserModel user, StorieModel storie){
        Optional<LikeModel> existingLike = likeRepository.findByUserAndStorie(user, storie);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return false;
        }else{
            var like = LikeModel.builder()
                .user(user)
                .storie(storie)
                .build();
            likeRepository.save(like);
            return true;
        }
    }
    
    public List<LikeModel> getLikedUserPosts(UserModel user) {
        List<LikeModel> likes = likeRepository.findLikesByUserOrderByDescendingId(user);
        return likes;
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

    public void deleteAllLikesPosts(PostModel postModel) {
        List<LikeModel> likes = likeRepository.findByPost(postModel);
        likeRepository.deleteAll(likes);
    }

    public void deleteAllLikesStories(StorieModel storieModel) {
        List<LikeModel> likes = likeRepository.findByStorie(storieModel);
        likeRepository.deleteAll(likes);
    }

    public void deleteAllLikesUser(UserModel userModel) {
        List<LikeModel> likes = likeRepository.findByUser(userModel);
        likeRepository.deleteAll(likes);
    }

    public List<UserLikePost> findTop3UsersWhoLikedPost(UUID postId) {
        List<LikeModel> topLikes = likeRepository.findTop3ByPostIdOrderByCreatedAtAsc(postId);
        List<UserLikePost> topUsersWhoLikedPost = new ArrayList<>();

        for (LikeModel like : topLikes) {
            UserModel user = like.getUser();
            topUsersWhoLikedPost.add(new UserLikePost(user));
        }

        return topUsersWhoLikedPost;
    }

    public void deleteAllComments(UUID commentId){
        List<LikeModel> likesForComment = likeRepository.findByCommentId(commentId);
        likeRepository.deleteAll(likesForComment);
    }

    public void deleteLikes(List<LikeModel> likes) {
        likeRepository.deleteAll(likes);
    }
}
