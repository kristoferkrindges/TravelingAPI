package com.kristofer.traveling.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.FollowingModel;

public interface FollowingRepository extends JpaRepository<FollowingModel,  Long> {
    Optional<FollowingModel> findByFollowingAndFollowerId(Long follower_id, Long following_id);
    List<FollowingModel> findByFollowerId(Long userId);
}
