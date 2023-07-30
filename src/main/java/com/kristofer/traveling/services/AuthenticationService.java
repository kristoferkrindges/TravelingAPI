package com.kristofer.traveling.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kristofer.traveling.services.JwtService;
import com.kristofer.traveling.services.exceptions.ObjectAlreadyExistsException;
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
        //this.verifyEmailExists(request.getEmail());
        //this.verifyAtExists(request.getAt());
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
        var user = this.findByEmail(request.getEmail())
            .orElseThrow();
        //Duplicated
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    private void verifyEmailExists(String email){
        System.out.println(this.findByEmail(email));
        if(this.findByEmail(email) != null){
            throw new ObjectAlreadyExistsException(
                "Email already registered in the system");
        }
    }

    private void verifyAtExists(String at){
        if(this.findByAt(at) != null){
            throw new ObjectAlreadyExistsException(
                "At already registered in the system");
        }
    }

    public Optional<UserModel> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<UserModel> findByAt(String at){
        return userRepository.findByAt(at);
    }
}
