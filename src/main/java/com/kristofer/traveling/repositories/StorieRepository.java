package com.kristofer.traveling.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kristofer.traveling.models.StorieModel;
import com.kristofer.traveling.models.UserModel;

public interface StorieRepository extends JpaRepository<StorieModel,  UUID> {
    List<StorieModel> findByCreatorOrderByDatePublicDesc(UserModel creator);

    @Query("SELECT DISTINCT u FROM UserModel u JOIN FETCH u.stories s ORDER BY s.createdAt DESC")
    List<UserModel> findAllUsersWithStoriesOrderByLatestStory();
}
