package com.kristofer.traveling.services.event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.requests.event.EventRequest;
import com.kristofer.traveling.dtos.responses.event.EventResponse;
import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.EventModel;
import com.kristofer.traveling.models.LikeModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.models.Enums.NotificationTypeEnum;
import com.kristofer.traveling.repositories.EventRepository;
import com.kristofer.traveling.services.FollowerService;
import com.kristofer.traveling.services.NotificationService;
import com.kristofer.traveling.services.attend.AttendService;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;
import com.kristofer.traveling.services.exceptions.ObjectNotNullException;
import com.kristofer.traveling.services.exceptions.ObjectNotPermission;
import com.kristofer.traveling.services.users.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final AttendService attendService;
    private final NotificationService notificationService;
    private final FollowerService followerService;

    public List<EventResponse> findAll(String token){
        // List<EventModel> events = eventRepository.findAllUpcomingEventsOrderByEventDate();
        List<EventModel> events = eventRepository.findAllByOrderByCreatedAtDesc();
        List<EventResponse> eventsResponse = events.stream().map(x-> new EventResponse(
            x, this.pressAttend(token, x), attendService.findTop3UsersWhoAttendEvent(x.getId())))
        .collect(Collectors.toList());
        return eventsResponse;
    }

    public List<EventResponse> findMyEvents(String token){
        UserModel user = userService.userByToken(token);
        List<EventModel> events = eventRepository.findByCreatorId(user.getId());
        List<EventResponse> eventsResponse = events.stream().map(x-> new EventResponse(
            x, this.pressAttend(token, x), attendService.findTop3UsersWhoAttendEvent(x.getId())))
        .collect(Collectors.toList());
        return eventsResponse;
    }

    public List<EventResponse> findMyEventsAttend(String token){
        UserModel user = userService.userByToken(token);
        List<EventModel> events = attendService.findEventsAttendedByUser(user);
        List<EventResponse> eventsResponse = events.stream().map(x-> new EventResponse(
            x, this.pressAttend(token, x), attendService.findTop3UsersWhoAttendEvent(x.getId())))
        .collect(Collectors.toList());
        return eventsResponse;
    }

    public List<EventResponse> findEventsNowMonth(String token) {
        LocalDateTime currentDate = LocalDateTime.now();
        Month currentMonth = currentDate.getMonth();
        
        List<EventModel> events = eventRepository.findAllUpcomingEventsOrderByEventDate();
        events = events.stream()
                .filter(event -> event.getEventDate().getMonth() == currentMonth)
                .collect(Collectors.toList());
        
        List<EventResponse> eventsResponse = events.stream()
                .map(x -> new EventResponse(
                        x, this.pressAttend(token, x), attendService.findTop3UsersWhoAttendEvent(x.getId())))
                .collect(Collectors.toList());
    
        return eventsResponse;
    }

    public List<UserAllResponse> findUsersEvent(String token, Long eventId){
        List<UserModel> users = attendService.findUsersByEventId(eventId);
        UserModel userOwner = userService.userByToken(token);
        List<UserAllResponse> usersAllResponse = users.stream().map(x-> new UserAllResponse(x, followerService.searchFollower(userOwner, x)))
        .collect(Collectors.toList());
        return usersAllResponse;
    }

    // public List<EventResponse> findRandomEvents(String token) {
    //     List<EventModel> allEvents = eventRepository.findAll();

    //     List<EventResponse> eventsResponse = allEvents.stream().map(x-> new EventResponse(
    //         x, this.pressAttend(token, x), attendService.findTop3UsersWhoAttendEvent(x.getId())))
    //     .collect(Collectors.toList());
        
    //     Random random = new Random();
    //     int numberOfRandomEvents = Math.min(2, allEvents.size());
    //     return random.ints(0, eventsResponse.size())
    //             .distinct()
    //             .limit(numberOfRandomEvents)
    //             .mapToObj(eventsResponse::get)
    //             .collect(Collectors.toList());
    // }

    public List<EventResponse> findRandomEvents(String token) {
        List<EventModel> allEvents = eventRepository.findAll();
    
        if (allEvents.isEmpty()) {
            return List.of();
        }
    
        List<EventResponse> eventsResponse = allEvents.stream()
                .map(x -> new EventResponse(
                        x, this.pressAttend(token, x), attendService.findTop3UsersWhoAttendEvent(x.getId())))
                .collect(Collectors.toList());
    
        Random random = new Random();
        int numberOfRandomEvents = Math.min(2, allEvents.size());
    
        List<EventResponse> randomEvents = random.ints(0, eventsResponse.size())
                .distinct()
                .limit(numberOfRandomEvents)
                .mapToObj(eventsResponse::get)
                .collect(Collectors.toList());
    
        return randomEvents;
    }

    public EventResponse insert(String token, EventRequest request){
        EventModel event = this.createdEventModel(token, request);
        eventRepository.save(event);
        return new EventResponse(event);
    }

    public EventResponse update(String token, EventRequest request, Long id){
        EventModel event = this.updateDataEvent(token, request, id);
        eventRepository.save(event);
        return new EventResponse(event);
    }

    public String delete(String token, Long id){
        EventModel eventModel = this.verifyEventExistId(id);
        this.verifyIdUser(token, eventModel.getCreator().getId());
        attendService.deleteAllAttendEvents(eventModel);
        this.eventRepository.delete(eventModel);
        return "Delete with sucess!";
    }

    public void deleteAllEventsUser(UserModel user){
        List<EventModel> events = eventRepository.findByCreator(user);
        eventRepository.deleteAll(events);
        
    }

    public void toogleAttendEvent(String token, Long eventId){
        UserModel user = userService.userByToken(token);
        EventModel eventModel = this.verifyEventExistId(eventId);
        Boolean verify = attendService.toggleAttend(user, eventModel);
        if(verify){
            notificationService.createNotification(eventModel.getCreator(), NotificationTypeEnum.ATTENDEVENT, user, eventModel.getId());
        }
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private EventModel updateDataEvent(String token, EventRequest request, Long id){
        EventModel eventModel = this.verifyEventExistId(id);
        this.verifyRequestInsert(request);
        this.verifyIdUser(token, eventModel.getCreator().getId());
        eventModel.setPhoto(request.getPhoto());
        eventModel.setName(request.getName());
        eventModel.setEventDate(this.convertStringToDateTime(request.getEventDate()));
        eventModel.setDetails(request.getDetails());
        eventModel.setAddress(request.getAddress());
        eventModel.setType(request.getType());
        eventModel.setPrice(request.getPrice());
        eventModel.setZipCode(request.getZipCode());
        eventModel.setCity(request.getCity());
        eventModel.setEdit(true);
        return eventModel;
    }

    private void verifyIdUser(String token, Long id){
        UserModel user = userService.userByToken(token);
        if(user.getId() != id){
            throw new ObjectNotPermission("This user does not have permission to change Post");
        }
    }

    private EventModel verifyEventExistId(Long id){
        Optional<EventModel> event = eventRepository.findById(id);
        return event.orElseThrow(
            ()-> new ObjectNotFoundException("Event with id " + id + " not found"));
    }

    private EventModel createdEventModel(String token, EventRequest request){
        this.verifyRequestInsert(request);
        UserModel user = userService.userByToken(token);
        var event = EventModel.builder()
            .name(request.getName())
            .photo(request.getPhoto())
            .city(request.getCity())
            .address(request.getAddress())
            .zipCode(request.getZipCode())
            .type(request.getType())
            .price(request.getPrice())
            .details(request.getDetails())
            .eventDate(this.convertStringToDateTime(request.getEventDate()))
            .creator(user)
            .edit(false)
            .build();
        return event;
    }

    private LocalDateTime convertStringToDateTime(String date){
        LocalDateTime eventDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        return eventDate;
    }

    private void verifyRequestInsert(EventRequest request){
        if(request.getCreatorId() == null){
            throw new ObjectNotNullException("Creator: Creator is required.");
        }
        if(request.getName() == null){
            throw new ObjectNotNullException("Name: Name is required.");
        }
        if(request.getCity() == null){
            throw new ObjectNotNullException("City: City is required.");
        }
        if(request.getAddress() == null){
            throw new ObjectNotNullException("Address: Address is required.");
        }
        if(request.getZipCode() == null){
            throw new ObjectNotNullException("Zip Code: Zip Code is required.");
        }
        if(request.getType() == null){
            throw new ObjectNotNullException("Type: Type is required.");
        }
    }

    private boolean pressAttend(String token, EventModel event){
        UserModel user = userService.userByToken(token);
        return attendService.pressAttend(user, event);
    }
}
