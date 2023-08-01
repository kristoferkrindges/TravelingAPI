package com.kristofer.traveling.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.FollowerModel;

public interface FollowerRepository extends JpaRepository<FollowerModel,  Long> {
    
}
