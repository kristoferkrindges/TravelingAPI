package com.kristofer.traveling.services;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.repositories.FollowerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowerService {
    
    private final FollowerRepository followerRepository;
}
