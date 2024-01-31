package com.kristofer.traveling.repositories;

import com.kristofer.traveling.models.PostModel;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostModel,  UUID> {
    List<PostModel> findByCreatorId(UUID userId);
}
