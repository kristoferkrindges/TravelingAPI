package com.kristofer.traveling.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kristofer.traveling.models.EventModel;

public interface EventRepository extends JpaRepository<EventModel, Long> {
    
    @Query("SELECT e FROM EventModel e WHERE e.eventDate > CURRENT_DATE ORDER BY e.eventDate ASC")
    List<EventModel> findAllUpcomingEventsOrderByEventDate();

    List<EventModel> findByCreatorId(Long userId);
}