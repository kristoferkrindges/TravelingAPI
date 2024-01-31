package com.kristofer.traveling.dtos.requests.post;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private String img;
    private String phrase;
    private Date datePublic;
    private UUID creatorId;
}
