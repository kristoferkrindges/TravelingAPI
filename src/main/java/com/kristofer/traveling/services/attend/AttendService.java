package com.kristofer.traveling.services.attend;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.responses.user.UserLikePost;
import com.kristofer.traveling.models.AttendModel;
import com.kristofer.traveling.models.EventModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.AttendRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendService {
    private final AttendRepository attendRepository;

    public void deleteAllAttendEvents(EventModel eventModel) {
        List<AttendModel> event = attendRepository.findByEvent(eventModel);
        attendRepository.deleteAll(event);
    }

    public void toggleAttend(UserModel user, EventModel eventModel){
        Optional<AttendModel> existingAttend = attendRepository.findByUserAndEvent(user, eventModel);
        if (existingAttend.isPresent()) {
            attendRepository.delete(existingAttend.get());
        }else{
            var attend = AttendModel.builder()
                .user(user)
                .event(eventModel)
                .build();
            attendRepository.save(attend);
        }
    }

    public boolean pressAttend(UserModel user, EventModel event) {
        Optional<AttendModel> existingAttend = attendRepository.findByUserAndEvent(user, event);
        if (existingAttend.isPresent()) {
            return true;
        }else{
            return false;
        }
    }

    public List<UserLikePost> findTop3UsersWhoAttendEvent(Long eventId) {
        List<AttendModel> topAttend = attendRepository.findTop3ByEventIdOrderByCreatedAtAsc(eventId);
        List<UserLikePost> topUsersAttend = new ArrayList<>();
        for (AttendModel attend : topAttend) {
            UserModel user = attend.getUser();
            topUsersAttend.add(new UserLikePost(user));
        }
        return topUsersAttend;
    }

    public List<EventModel> findEventsAttendedByUser(UserModel user) {
        List<AttendModel> attendances = attendRepository.findByUser(user);

        return attendances.stream()
                .map(AttendModel::getEvent)
                .collect(Collectors.toList());
    }

}
