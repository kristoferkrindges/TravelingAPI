package com.kristofer.traveling.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.FollowingModel;

public interface FollowingRepository extends JpaRepository<FollowingModel,  Long> {
    
}
