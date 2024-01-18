package com.kristofer.traveling.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kristofer.traveling.models.FollowingModel;
import com.kristofer.traveling.models.UserModel;



public interface FollowingRepository extends JpaRepository<FollowingModel,  UUID> {
    Optional<FollowingModel> findByFollowerAndFollowing(UserModel follower, UserModel following);
    List<FollowingModel> findByFollowerId(UUID userId);

    @Query("SELECT f FROM FollowingModel f WHERE f.follower.id = :userId ORDER BY f.id DESC")
    List<FollowingModel> findByFollowerIdOrderByidDesc(@Param("userId") UUID userId);
    String deleteByFollowerOrFollowing(UserModel userOne, UserModel userTwo);

    List<FollowingModel> findByFollowerOrFollowing(UserModel follower, UserModel following);
}
