package com.kristofer.traveling.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.NotificationModel;

public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {

    List<NotificationModel> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<NotificationModel> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);
    long countByUserIdAndReadFalse(Long userId);
    
}
