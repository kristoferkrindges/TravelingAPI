package com.kristofer.traveling.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.ConfigurationModel;


public interface ConfigurationRepository extends JpaRepository<ConfigurationModel, UUID> {
    Optional<ConfigurationModel> findByUserId(UUID userId);
}
