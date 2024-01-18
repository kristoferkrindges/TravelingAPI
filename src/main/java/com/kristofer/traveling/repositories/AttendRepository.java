package com.kristofer.traveling.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kristofer.traveling.models.AttendModel;
import com.kristofer.traveling.models.EventModel;
import com.kristofer.traveling.models.UserModel;

public interface AttendRepository extends JpaRepository<AttendModel, UUID>{
    List<AttendModel> findByUser(UserModel user);
    List<AttendModel> findByEvent(EventModel event);
    List<AttendModel> findTop3ByEventIdOrderByCreatedAtAsc(UUID eventId);
    Optional<AttendModel> findByUserAndEvent(UserModel user, EventModel event);
    List<AttendModel> findByEventId(UUID eventId);
}
