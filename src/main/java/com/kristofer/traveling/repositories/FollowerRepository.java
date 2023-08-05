package com.kristofer.traveling.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.FollowerModel;

public interface FollowerRepository extends JpaRepository<FollowerModel,  Long> {
    Optional<FollowerModel> findByFollowingAndFollowerId(Long follower_id, Long following_id);

    List<FollowerModel> findByFollowingId(Long userId);
}
