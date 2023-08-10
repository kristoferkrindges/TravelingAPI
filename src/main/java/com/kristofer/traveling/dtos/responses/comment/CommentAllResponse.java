package com.kristofer.traveling.dtos.responses.comment;

import java.util.Date;

import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.CommentModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentAllResponse {
    private Long id;
    private String phrase;
    private String img;
    private Date datepublic;
    private UserAllResponse userAllResponse;
    private Long postId;
    private Long parentComment;
    private Integer likes;
    private Integer comments;

    public CommentAllResponse(CommentModel commentModel){
        this.id = commentModel.getId();
        this.phrase = commentModel.getPhrase();
        this.img = commentModel.getImg();
        this.datepublic = commentModel.getDatePublic();
        this.userAllResponse = new UserAllResponse(commentModel.getCreator());
        this.postId = commentModel.getPost() != null ? commentModel.getPost().getId() : null;
        this.parentComment = commentModel.getParentComment() != null ? commentModel.getParentComment().getId() : null;
        //this.likes = post.getLikes() != null ? post.getLikes().size() : 0;
        this.comments = commentModel.getChildComments() != null ? commentModel.getChildComments().size() : 0;
    }
}
