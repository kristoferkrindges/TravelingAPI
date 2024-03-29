package com.kristofer.traveling.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.UserModel;


public interface UserRepository extends JpaRepository<UserModel,  UUID> {
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByAt(String at);
}
