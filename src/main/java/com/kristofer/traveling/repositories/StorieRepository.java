package com.kristofer.traveling.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.StorieModel;

public interface StorieRepository extends JpaRepository<StorieModel,  Long> {
    
}
