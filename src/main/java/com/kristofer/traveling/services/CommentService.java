package com.kristofer.traveling.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.requests.comment.CommentRequest;
import com.kristofer.traveling.dtos.responses.comment.CommentAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.CommentModel;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.models.Enums.NotificationTypeEnum;
import com.kristofer.traveling.repositories.CommentRepository;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;
import com.kristofer.traveling.services.exceptions.ObjectNotNullException;
import com.kristofer.traveling.services.exceptions.ObjectNotPermission;
import com.kristofer.traveling.services.users.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final LikeService likeService;
    private final PostService postService;
    private final NotificationService notificationService;

    public List<CommentAllResponse> findAll(){
        List<CommentModel> comments = commentRepository.findAll();
        List<CommentAllResponse> commentAllResponse = comments.stream().map(x-> new CommentAllResponse(x))
        .collect(Collectors.toList());
        return commentAllResponse;
    }

    public CommentAllResponse findById(Long id){
        CommentModel commentModel = this.findComment(id);
        CommentAllResponse comment = new CommentAllResponse(commentModel);
        return comment;
    }

    public CommentAllResponse insert(String token, CommentRequest request){
        CommentModel comment = this.createdCommentModel(token, request);
        commentRepository.save(comment);
        return new CommentAllResponse(comment);
    }

    public CommentAllResponse update(String token, CommentRequest request, Long id){
        CommentModel comment = this.updateDataComment(token, request, id);
        commentRepository.save(comment);
        return new CommentAllResponse(comment);
    }

    public String delete(String token, Long id){
        CommentModel commentToDelete = this.verifyCommentExistId(id);
        this.verifyIdUser(token, commentToDelete.getCreator().getId());
        if (commentToDelete.getParentComment() != null){
            List<CommentModel> childComments = commentRepository.findByParentCommentId(commentToDelete.getId());
            for (CommentModel childComment : childComments) {
                // recursive call
                delete(token, childComment.getId());
            }
        }
        this.commentRepository.delete(commentToDelete);
        return "Delete with sucess!";
    }

    public List<CommentAllResponse> getPostComments(Long postId){
        postService.findByIdPost(postId);
        List<CommentModel> comments = commentRepository.findByPostId(postId);
        List<CommentAllResponse> commentAllResponse = comments.stream().map(x-> new CommentAllResponse(x))
        .collect(Collectors.toList());
        return commentAllResponse;
    }

    public List<CommentAllResponse> getChildComment(Long parentCommentId){
        List<CommentModel> comments = commentRepository.findByParentCommentId(parentCommentId);
        List<CommentAllResponse> commentAllResponse = comments.stream().map(x-> new CommentAllResponse(x))
        .collect(Collectors.toList());
        return commentAllResponse;
    }

    public void toogleLikeComment(String token, Long commentId){
        UserModel user = userService.userByToken(token);
        CommentModel comment = this.verifyCommentExistId(commentId);
        likeService.toggleLikeComment(user, comment);
        notificationService.createNotification(comment.getCreator(), NotificationTypeEnum.LIKEPOST, user, comment.getId());
    }

    public List<UserAllResponse> getLikedCommentsUser(Long commentId) {
        CommentModel comment = this.verifyCommentExistId(commentId);
        return likeService.getLikedCommentsUser(comment);
    }

    private CommentModel updateDataComment(String token, CommentRequest request, Long id) {
        CommentModel commentModel = this.verifyCommentExistId(id);
        this.verifyRequestUpdate(request);
        this.verifyIdUser(token, commentModel.getCreator().getId());
        commentModel.setImg(request.getImg());
        commentModel.setPhrase(request.getPhrase());
        commentModel.setDatePublic(request.getDatePublic());
        return commentModel;
    }

    private void verifyIdUser(String token, Long id){
        UserModel user = userService.userByToken(token);
        if(user.getId() != id){
            throw new ObjectNotPermission("This user does not have permission to change Post");
        }
    }

    private CommentModel verifyCommentExistId(Long id) {
        Optional<CommentModel> comment = commentRepository.findById(id);
        return comment.orElseThrow(
            ()-> new ObjectNotFoundException("Comment with id " + id + " not found"));
    }

    private CommentModel createdCommentModel(String token, CommentRequest request) {
        this.verifyRequestInsert(request);
        UserModel user = userService.userByToken(token);
        var comment = CommentModel.builder()
            .img(request.getImg())
            .phrase(request.getPhrase())
            .datePublic(request.getDatePublic())
            .creator(user)
            .build();
        if (request.getParentComment() != null){
            CommentModel commentFather = this.verifyExistsComment(request);
            comment.setParentComment(commentFather);
            notificationService.createNotification(commentFather.getCreator(), NotificationTypeEnum.REPLYCOMMENT, user, commentFather.getId());
        }
        if (request.getPostId() != null){
            PostModel post = postService.findByIdPost(request.getPostId());
            comment.setPost(post);
            notificationService.createNotification(post.getCreator(), NotificationTypeEnum.COMMENTPOST, user, post.getId());
        }
        return comment;
    }

    private CommentModel verifyExistsComment(CommentRequest request){
        return commentRepository.findById(request.getParentComment())
                .orElseThrow(() -> new ObjectNotFoundException("Comment with id " + request.getParentComment() + " not found"));
    }

    private void verifyRequestUpdate(CommentRequest request) {
        if(request.getDatePublic() == null){
            throw new ObjectNotNullException("DatePublic: DatePublic is required.");
        }
    }

    private void verifyRequestInsert(CommentRequest request) {
        if(!(request.getImg() != null || request.getPhrase() != null)){
            throw new ObjectNotNullException("Nothing to publish.");
        }
        if(request.getDatePublic() == null){
            throw new ObjectNotNullException("DatePublic: DatePublic is required.");
        }
        if(!(request.getPostId() != null || request.getParentComment() != null)){
            throw new ObjectNotNullException("PostId or ParentComment is required.");
        }
    }

    private CommentModel findComment(Long id) {
        Optional<CommentModel> commentModel = commentRepository.findById(id);
        if(!commentModel.isPresent()){
            throw new ObjectNotFoundException("Comment with id " + id + " not found");
        }
        return commentModel.get();
    }
}
