package com.kristofer.traveling.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kristofer.traveling.models.FavoriteModel;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.UserModel;

public interface FavoriteRepository extends JpaRepository<FavoriteModel, UUID> {
    List<FavoriteModel> findByUser(UserModel user);
    List<FavoriteModel> findByPost(PostModel post);
    @Query("SELECT f FROM FavoriteModel f WHERE f.user = :user ORDER BY f.id DESC")
    List<FavoriteModel> findFavoritesByUserOrderByDescendingId(UserModel user);
    Optional<FavoriteModel> findByUserAndPost(UserModel user, PostModel post);
}
