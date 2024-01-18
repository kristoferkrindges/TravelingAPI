package com.kristofer.traveling.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kristofer.traveling.models.EventModel;
import com.kristofer.traveling.models.UserModel;

public interface EventRepository extends JpaRepository<EventModel, UUID> {
    
    @Query("SELECT e FROM EventModel e WHERE e.eventDate > CURRENT_DATE ORDER BY e.eventDate ASC")
    List<EventModel> findAllUpcomingEventsOrderByEventDate();
    List<EventModel> findAllByOrderByCreatedAtDesc();
    List<EventModel> findByCreatorId(UUID userId);
    List<EventModel> findByCreator(UserModel creator);
}