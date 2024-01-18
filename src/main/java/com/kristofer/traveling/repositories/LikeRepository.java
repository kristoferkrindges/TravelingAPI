package com.kristofer.traveling.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kristofer.traveling.models.CommentModel;
import com.kristofer.traveling.models.LikeModel;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.StorieModel;
import com.kristofer.traveling.models.UserModel;

public interface LikeRepository extends JpaRepository<LikeModel, UUID> {
    List<LikeModel> findByUser(UserModel user);
    List<LikeModel> findByPost(PostModel post);
    List<LikeModel> findByStorie(StorieModel storieModel);
    Optional<LikeModel> findByUserAndPost(UserModel user, PostModel post);
    Optional<LikeModel> findByUserAndComment(UserModel user, CommentModel comment);
    Optional<LikeModel> findByUserAndStorie(UserModel user, StorieModel storie);
    List<LikeModel> findByComment(CommentModel comment);
    List<LikeModel> findByCommentId(UUID commentId);
    @Query("SELECT f FROM LikeModel f WHERE f.user = :user AND f.post IS NOT NULL ORDER BY f.id DESC")
    List<LikeModel> findLikesByUserOrderByDescendingId(UserModel user);
    List<LikeModel> findTop3ByPostIdOrderByCreatedAtAsc(UUID postId);
}
