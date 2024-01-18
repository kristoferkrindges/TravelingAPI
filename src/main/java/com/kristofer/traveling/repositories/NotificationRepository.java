package com.kristofer.traveling.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.NotificationModel;
import com.kristofer.traveling.models.UserModel;

public interface NotificationRepository extends JpaRepository<NotificationModel, UUID> {

    List<NotificationModel> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<NotificationModel> findByUserIdAndReadFalseOrderByCreatedAtDesc(UUID userId);
    List<NotificationModel> findByUserId(UUID userId);
    UUID countByUserIdAndReadFalse(UUID userId);
    List<NotificationModel> findByCreatorOrUser(UserModel creator, UserModel user);
}
