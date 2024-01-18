package com.kristofer.traveling.dtos.requests.storie;

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
public class StorieRequest {
    private String video;
    private Date datePublic;
    private UUID creatorId;
}
