package com.kristofer.traveling.repositories;

import com.kristofer.traveling.models.PostModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostModel,  Long> {
    
}
