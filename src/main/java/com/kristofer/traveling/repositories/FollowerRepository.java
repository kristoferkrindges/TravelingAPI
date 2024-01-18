package com.kristofer.traveling.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kristofer.traveling.models.FollowerModel;
import com.kristofer.traveling.models.UserModel;

public interface FollowerRepository extends JpaRepository<FollowerModel,  UUID> {
    Optional<FollowerModel> findByFollowerAndFollowing(UserModel follower, UserModel following);

    List<FollowerModel> findByFollowingId(UUID userId);

    @Query("SELECT f FROM FollowerModel f WHERE f.following.id = :userId ORDER BY f.id DESC")
    List<FollowerModel> findByFollowingIdOrderByidDesc(@Param("userId") UUID userId);

    List<FollowerModel> findByFollowingOrFollower(UserModel following, UserModel follower);
}
