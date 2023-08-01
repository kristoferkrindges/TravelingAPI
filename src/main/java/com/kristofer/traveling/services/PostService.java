package com.kristofer.traveling.services;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.repositories.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
}
