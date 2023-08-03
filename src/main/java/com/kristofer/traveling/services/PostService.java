package com.kristofer.traveling.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.requests.post.PostRequest;
import com.kristofer.traveling.dtos.responses.post.PostAllResponse;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.PostRepository;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;
import com.kristofer.traveling.services.exceptions.ObjectNotNullException;
import com.kristofer.traveling.services.exceptions.ObjectNotPermission;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final UserService userService;

    public List<PostAllResponse> findAll(){
        List<PostModel> posts = postRepository.findAll();
        List<PostAllResponse> postAllResponse = posts.stream().map(x-> new PostAllResponse(x))
        .collect(Collectors.toList());
        return postAllResponse;
    }

    public PostAllResponse findById(Long id){
        PostModel postModel = this.findPost(id);
        PostAllResponse post = new PostAllResponse(postModel);
        return post;
    }

    public PostAllResponse insert(String token, PostRequest request){
        PostModel post = this.createdPostModel(token, request);
        postRepository.save(post);
        return new PostAllResponse(post);
    }

    public PostAllResponse update(String token, PostRequest request, Long id){
        PostModel post = this.updateDataPost(token, request, id);
        postRepository.save(post);
        return new PostAllResponse(post);
    }

    public String delete(String token, Long id){
        PostModel postModel = this.findPost(id);
        this.verifyIdUser(token, postModel.getCreator().getId());
        this.postRepository.delete(postModel);
        return "Delete with sucess!";
    }

    private PostModel updateDataPost(String token, PostRequest request, Long id){
        PostModel postModel = this.verifyPostExistId(id);
        this.verifyRequestUpdate(request);
        this.verifyIdUser(token, request.getCreatorId());
        postModel.setImg(request.getImg());
        postModel.setPhrase(request.getPhrase());
        postModel.setDatePublic(request.getDatePublic());
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
            .build();
        return post;
    }

    private PostModel findPost(Long id){
        Optional<PostModel> postModel = postRepository.findById(id);
        if(!postModel.isPresent()){
            throw new ObjectNotFoundException("Post with id " + id + " not found");
        }
        return postModel.get();
    }

    private void verifyIdUser(String token, Long id){
        UserModel user = userService.userByToken(token);
        if(user.getId() != id){
            throw new ObjectNotPermission("This user does not have permission to change Post");
        }
    }

    private void verifyRequestUpdate(PostRequest request){
        if(request.getDatePublic() == null){
            throw new ObjectNotNullException("DatePublic: DatePublic is required.");
        }
        if(request.getCreatorId() == null){
            throw new ObjectNotNullException("Creator ID: Creator ID is required.");
        }
    }

    private PostModel verifyPostExistId(Long id){
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
