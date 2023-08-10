package com.kristofer.traveling.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.CommentModel;

public interface CommentRepository extends JpaRepository<CommentModel, Long> {
    List<CommentModel> findByPostId(Long postId);
    
    List<CommentModel> findByParentCommentId(Long parentCommentId);
}
