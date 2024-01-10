package com.kristofer.traveling.dtos.requests.event;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    private String name;
    private String eventDate;
    private String city;
    private String address;
    private String zipCode;
    private String photo;
    private String details;
    private String type;
    private String price;
    private Long creatorId;
}
