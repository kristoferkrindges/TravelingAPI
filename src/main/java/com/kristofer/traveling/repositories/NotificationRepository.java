package com.kristofer.traveling.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.NotificationModel;
import com.kristofer.traveling.models.UserModel;

public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {

    List<NotificationModel> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<NotificationModel> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);
    List<NotificationModel> findByUserId(Long userId);
    long countByUserIdAndReadFalse(Long userId);
    List<NotificationModel> findByCreatorOrUser(UserModel creator, UserModel user);
}
