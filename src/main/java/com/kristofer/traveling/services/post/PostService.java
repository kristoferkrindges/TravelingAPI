package com.kristofer.traveling.services.post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.Collections;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.requests.post.PostRequest;
import com.kristofer.traveling.dtos.responses.post.PostAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.PostRepository;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;
import com.kristofer.traveling.services.exceptions.ObjectNotNullException;
import com.kristofer.traveling.services.exceptions.ObjectNotPermission;
import com.kristofer.traveling.services.favorite.FavoriteService;
import com.kristofer.traveling.services.like.LikeService;
import com.kristofer.traveling.services.users.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final UserService userService;
    private final LikeService likeService;
    private final FavoriteService favoriteService;

    public List<PostAllResponse> findAll(String token){
        List<PostModel> posts = postRepository.findAll();
        Collections.sort(posts, Comparator.comparing(PostModel::getDatePublic).reversed());
        List<PostAllResponse> postAllResponse = posts.stream().map(x-> new PostAllResponse(
            x, this.pressLike(token, x), this.pressFavorite(token, x), likeService.findTop3UsersWhoLikedPost(x.getId())))
        .collect(Collectors.toList());
        return postAllResponse;
    }

    public PostAllResponse findById(UUID id, String token){
        PostModel postModel = this.findPost(id);
        PostAllResponse post = new PostAllResponse(
            postModel, this.pressLike(token, postModel), this.pressFavorite(token, postModel), likeService.findTop3UsersWhoLikedPost(id));
        return post;
    }
 

    public PostAllResponse insert(String token, PostRequest request){
        PostModel post = this.createdPostModel(token, request);
        postRepository.save(post);
        return new PostAllResponse(post);
    }

    public PostAllResponse update(String token, PostRequest request, UUID id){
        PostModel post = this.updateDataPost(token, request, id);
        postRepository.save(post);
        return new PostAllResponse(post);
    }

    public PostModel findByIdPost(UUID postId){
        return this.findPost(postId);
    }

    public List<UserAllResponse> allLikesPost(UUID postId, String token){
        PostModel post = this.findByIdPost(postId);
        return likeService.getLikedPostUsers(post, token);
    }

    public List<UserAllResponse> allFavoritesPost(UUID postId, String token){
        PostModel post = this.findByIdPost(postId);
        return favoriteService.getFavoritePostUsers(post, token);
    }

    public boolean pressLike(String token, PostModel post){
        UserModel user = userService.userByToken(token);
        return likeService.pressLike(user, post);
    }

    public boolean pressFavorite(String token, PostModel post){
        UserModel user = userService.userByToken(token);
        return favoriteService.pressFavorite(user, post);
    }

    public List<PostAllResponse> findByUserOwner(UUID id, String token) {
        List<PostModel> posts = postRepository.findByCreatorId(id);
        Collections.sort(posts, Comparator.comparing(PostModel::getDatePublic).reversed());
        List<PostAllResponse> postAllResponse = posts.stream().map(x-> new PostAllResponse(
            x, this.pressLike(token, x), this.pressFavorite(token, x), likeService.findTop3UsersWhoLikedPost(x.getId())))
        .collect(Collectors.toList());
        return postAllResponse;
    }

    private PostModel updateDataPost(String token, PostRequest request, UUID id){
        PostModel postModel = this.verifyPostExistId(id);
        this.verifyRequestUpdate(request);
        this.verifyIdUser(token, postModel.getCreator().getId());
        postModel.setImg(request.getImg());
        postModel.setPhrase(request.getPhrase());
        postModel.setDatePublic(request.getDatePublic());
        postModel.setEdit(true);
        return postModel;
    }

    private PostModel createdPostModel(String token, PostRequest request){
        this.verifyRequestInsert(request);
        UserModel user = userService.userByToken(token);
        var post = PostModel.builder()
            .img(request.getImg())
            .phrase(request.getPhrase())
            .datePublic(request.getDatePublic())
            .creator(user)
            .edit(false)
            .build();
        return post;
    }

    private PostModel findPost(UUID id){
        Optional<PostModel> postModel = postRepository.findById(id);
        if(!postModel.isPresent()){
            throw new ObjectNotFoundException("Post with id " + id + " not found");
        }
        return postModel.get();
    }

    private void verifyIdUser(String token, UUID id){
        UserModel user = userService.userByToken(token);
        if(user.getId() != id){
            throw new ObjectNotPermission("This user does not have permission to change Post");
        }
    }

    private void verifyRequestUpdate(PostRequest request){
        if(request.getDatePublic() == null){
            throw new ObjectNotNullException("DatePublic: DatePublic is required.");
        }
    }

    private PostModel verifyPostExistId(UUID id){
        Optional<PostModel> post = postRepository.findById(id);
        return post.orElseThrow(
            ()-> new ObjectNotFoundException("Post with id " + id + " not found"));
    }

    private void verifyRequestInsert(PostRequest request){
        if(!(request.getImg() != null || request.getPhrase() != null)){
            throw new ObjectNotNullException("Nothing to publish.");
        }
        if(request.getDatePublic() == null){
            throw new ObjectNotNullException("DatePublic: DatePublic is required.");
        }
    }
}
