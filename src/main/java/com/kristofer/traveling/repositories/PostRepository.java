package com.kristofer.traveling.repositories;

import com.kristofer.traveling.models.PostModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostModel,  Long> {
    List<PostModel> findByCreatorId(Long userId);
}
