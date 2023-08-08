package com.kristofer.traveling.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.FavoriteModel;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.UserModel;

public interface FavoriteRepository extends JpaRepository<FavoriteModel, Long> {
    List<FavoriteModel> findByUser(UserModel user);
    List<FavoriteModel> findByPost(PostModel post);
    Optional<FavoriteModel> findByUserAndPost(UserModel user, PostModel post);
}
