package com.kristofer.traveling.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kristofer.traveling.services.JwtService;
import com.kristofer.traveling.controllers.user.requests.AuthenticationRequest;
import com.kristofer.traveling.controllers.user.requests.RegisterRequest;
import com.kristofer.traveling.controllers.user.responses.AuthenticationResponse;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.models.Enums.Role;
import com.kristofer.traveling.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    
    public AuthenticationResponse register(RegisterRequest request){
        // Validate email exists;
        var user = UserModel.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .at(request.getAt())
            .photo(request.getPhoto())
            .banner(request.getBanner())
            .birthdate(request.getBirthdate())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
            )
        );
        var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow();
        //Duplicated
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }
}
