package com.kristofer.traveling.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.ConfigurationModel;


public interface ConfigurationRepository extends JpaRepository<ConfigurationModel, Long> {
    Optional<ConfigurationModel> findByUserId(Long userId);
}
