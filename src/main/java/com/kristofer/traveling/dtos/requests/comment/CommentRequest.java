package com.kristofer.traveling.dtos.requests.comment;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private String img;
    private String phrase;
    private Date datePublic;
    private Long creatorId;
    private Long postId;
    private Long parentComment;
}
