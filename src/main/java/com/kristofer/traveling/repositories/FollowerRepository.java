package com.kristofer.traveling.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kristofer.traveling.models.FollowerModel;
import com.kristofer.traveling.models.UserModel;

public interface FollowerRepository extends JpaRepository<FollowerModel,  Long> {
    Optional<FollowerModel> findByFollowerAndFollowing(UserModel follower, UserModel following);

    List<FollowerModel> findByFollowingId(Long userId);

    @Query("SELECT f FROM FollowerModel f WHERE f.following.id = :userId ORDER BY f.id DESC")
    List<FollowerModel> findByFollowingIdOrderByidDesc(@Param("userId") Long userId);
}
