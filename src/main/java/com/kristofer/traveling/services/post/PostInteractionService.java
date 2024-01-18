package com.kristofer.traveling.services.post;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.PostRepository;
import com.kristofer.traveling.services.comment.CommentService;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;
import com.kristofer.traveling.services.exceptions.ObjectNotPermission;
import com.kristofer.traveling.services.favorite.FavoriteService;
import com.kristofer.traveling.services.like.LikeService;
import com.kristofer.traveling.services.users.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostInteractionService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final FavoriteService favoriteService;

    public String delete(String token, UUID id){
        PostModel postModel = this.findPost(id);
        this.verifyIdUser(token, postModel.getCreator().getId());
        commentService.deleteAllCommentsPosts(postModel);
        likeService.deleteAllLikesPosts(postModel);
        favoriteService.deleteAllFavoritesPosts(postModel);
        this.postRepository.delete(postModel);
        return "Delete with sucess!";
    }
    private void verifyIdUser(String token, UUID id){
        UserModel user = userService.userByToken(token);
        if(user.getId() != id){
            throw new ObjectNotPermission("This user does not have permission to change Post");
        }
    }

    private PostModel findPost(UUID id){
        Optional<PostModel> postModel = postRepository.findById(id);
        if(!postModel.isPresent()){
            throw new ObjectNotFoundException("Post with id " + id + " not found");
        }
        return postModel.get();
    }
}
