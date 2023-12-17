package com.kristofer.traveling.services.event;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.requests.event.EventRequest;
import com.kristofer.traveling.dtos.responses.event.EventResponse;
import com.kristofer.traveling.models.EventModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.models.Enums.NotificationTypeEnum;
import com.kristofer.traveling.repositories.EventRepository;
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
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        List<EventModel> events = eventRepository.findAllUpcomingEventsOrderByEventDate();
        events = events.stream()
                .filter(event -> toLocalDate(event.getEventDate()).getMonth() == currentMonth)
                .collect(Collectors.toList());
        List<EventResponse> eventsResponse = events.stream()
                .map(x -> new EventResponse(
                        x, this.pressAttend(token, x), attendService.findTop3UsersWhoAttendEvent(x.getId())))
                .collect(Collectors.toList());

        return eventsResponse;
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

    public void toogleAttendEvent(String token, Long eventId){
        UserModel user = userService.userByToken(token);
        EventModel eventModel = this.verifyEventExistId(eventId);
        Boolean verify = attendService.toggleAttend(user, eventModel);
        if(verify){
            notificationService.createNotification(eventModel.getCreator(), NotificationTypeEnum.ATTENDEVENT, user, eventModel.getId());
        }
    }

    private LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private EventModel updateDataEvent(String token, EventRequest request, Long id){
        EventModel eventModel = this.verifyEventExistId(id);
        this.verifyRequestInsert(request);
        this.verifyIdUser(token, eventModel.getCreator().getId());
        eventModel.setPhoto(request.getPhoto());
        eventModel.setName(request.getName());
        eventModel.setEventDate(request.getEventDate());
        eventModel.setDetails(request.getDetails());
        eventModel.setAddress(request.getAddress());
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
            .price(request.getPrice())
            .details(request.getDetails())
            .eventDate(request.getEventDate())
            .creator(user)
            .edit(false)
            .build();
        return event;
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
    }

    private boolean pressAttend(String token, EventModel event){
        UserModel user = userService.userByToken(token);
        return attendService.pressAttend(user, event);
    }
}
