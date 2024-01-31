package com.kristofer.traveling.services.users;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kristofer.traveling.services.configuration.ConfigurationService;
import com.kristofer.traveling.services.exceptions.ObjectAlreadyExistsException;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;
import com.kristofer.traveling.services.exceptions.ObjectNotNullException;
import com.kristofer.traveling.services.jwt.JwtService;
import com.kristofer.traveling.dtos.requests.user.AuthenticationRequest;
import com.kristofer.traveling.dtos.requests.user.RegisterRequest;
import com.kristofer.traveling.dtos.responses.user.AuthenticationResponse;
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

    private final ConfigurationService configurationService;
    
    public AuthenticationResponse register(RegisterRequest request){
        UserModel user = this.registerData(request);
        userRepository.save(user);
        configurationService.creteConfiguration(user);
        return generatorToken(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        this.verifyEmailNotExists(request.getEmail());
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
            )
        );
        var user = this.findByEmail(request.getEmail())
            .orElseThrow();
        return generatorToken(user);
    }

    private AuthenticationResponse generatorToken(UserModel user){
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    private UserModel registerData(RegisterRequest request){
        this.validateValuesUpdate(request);
        this.verifyEmailExists(request.getEmail());
        this.verifyAtExists(request.getAt());
        var user = UserModel.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .at(request.getAt())
            .photo(request.getPhoto())
            .banner(request.getBanner())
            .birthdate(this.convertStringToDateTime(request.getBirthdate()))
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
        return user;
    }

    private LocalDateTime convertStringToDateTime(String date){
        LocalDateTime eventDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        return eventDate;
    }

    private void validateValuesUpdate(RegisterRequest request){
        if(request.getFirstname() == null){
            throw new ObjectNotNullException("Firstname: Firstname is required.");
        }
        if(request.getLastname() == null){
            throw new ObjectNotNullException("Lastname: Lastname is required");
            
        }
        if(request.getEmail() == null){
            throw new ObjectNotNullException("Email: Email is required");
            
        }
        if(request.getAt() == null){
            throw new ObjectNotNullException("At: At is required");
            
        }
        if(request.getBirthdate() == null){
            throw new ObjectNotNullException("Birthdate: Birthdate is required");
            
        }
        if(request.getPassword() == null){
            throw new ObjectNotNullException("Password: Password is required");
            
        }
        return;
    }

    private void verifyEmailNotExists(String email){
        if(this.findByEmail(email).isEmpty()){
            throw new ObjectNotFoundException(
                "Email not register in the system");
        }
    }

    private void verifyEmailExists(String email){
        if(this.findByEmail(email).isPresent()){
            throw new ObjectAlreadyExistsException(
                "Email already registered in the system");
        }
    }

    private void verifyAtExists(String at){
        if(this.findByAt(at).isPresent()){
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
