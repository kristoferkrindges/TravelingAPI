package com.kristofer.traveling.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.FollowerModel;
import com.kristofer.traveling.models.UserModel;

public interface FollowerRepository extends JpaRepository<FollowerModel,  Long> {
    Optional<FollowerModel> findByFollowerAndFollowing(UserModel follower, UserModel following);

    List<FollowerModel> findByFollowingId(Long userId);
}
