package com.kristofer.traveling.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.CommentModel;
import com.kristofer.traveling.models.LikeModel;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.UserModel;

public interface LikeRepository extends JpaRepository<LikeModel, Long> {
    List<LikeModel> findByUser(UserModel user);
    List<LikeModel> findByPost(PostModel post);
    Optional<LikeModel> findByUserAndPost(UserModel user, PostModel post);
    Optional<LikeModel> findByUserAndComment(UserModel user, CommentModel comment);
    List<LikeModel> findByComment(CommentModel comment);
}
