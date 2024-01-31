package com.kristofer.traveling.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.CommentModel;
import com.kristofer.traveling.models.PostModel;

public interface CommentRepository extends JpaRepository<CommentModel, UUID> {
    List<CommentModel> findByPostId(UUID postId);
    
    List<CommentModel> findByParentCommentId(UUID parentCommentId);

    List<CommentModel> findByPost(PostModel post);
}
