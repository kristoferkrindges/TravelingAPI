package com.kristofer.traveling.services.users;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.requests.user.PasswordsRequest;
import com.kristofer.traveling.dtos.requests.user.PhotoRequest;
import com.kristofer.traveling.dtos.requests.user.UserUpdateRequest;
import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.UserRepository;
import com.kristofer.traveling.services.exceptions.ObjectAlreadyExistsException;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;
import com.kristofer.traveling.services.exceptions.ObjectNotNullException;
import com.kristofer.traveling.services.exceptions.PasswordsNotSame;
import com.kristofer.traveling.services.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public List<UserAllResponse> findAll(){
        List<UserModel> users = userRepository.findAll();
        List<UserAllResponse> userAllResponse = users.stream().map(x-> new UserAllResponse(x))
        .collect(Collectors.toList());
        return userAllResponse;
    }

    public List<UserModel> findAllUserModel(){
        List<UserModel> users = userRepository.findAll();
        return users;
    }

    public UserModel findById(UUID id){
        Optional<UserModel> obj = userRepository.findById(id);
        return obj.orElseThrow(
            ()-> new ObjectNotFoundException("User with id " + id + " not found"));
    }

    public UserModel findByAt(String at){
        Optional<UserModel> obj = userRepository.findByAt(at);
        return obj.orElseThrow(
            ()-> new ObjectNotFoundException("User with at " + at + " not found"));
    }

    public UserModel checkUser(String token){
        UserModel user = userByToken(token);
        return user;
    }

    public UserModel update(String token, UserUpdateRequest request){
        UserModel userUpdate = userByToken(token);
        this.validateValuesUpdate(request);
        this.validateAtIsEqual(userUpdate, request.getAt());
        UserModel newUser = this.updateData(userUpdate, request);
        userRepository.save(newUser);
        return newUser;
    }

    public UserModel updatePhoto(String token, PhotoRequest request){
        UserModel userUpdate = userByToken(token);
        userUpdate.setPhoto(request.getImageUrl());
        userRepository.save(userUpdate);
        return userUpdate;
    }

    public UserModel updateBanner(String token, PhotoRequest request){
        UserModel userUpdate = userByToken(token);
        userUpdate.setBanner(request.getImageUrl());
        userRepository.save(userUpdate);
        return userUpdate;
    }

    public void updatePassword(String token, PasswordsRequest obj){
        UserModel user = userByToken(token);
        if(!this.verifyPasswordEncoder(user.getPassword(), obj.getPassword())){
            throw new PasswordsNotSame("Passwords are not the same");
        }
        String newPassword = passwordEncoder.encode(obj.getNewpassword());
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    private boolean verifyPasswordEncoder(String passwordDB, String passwordRequest){
        return bCryptPasswordEncoder.matches(passwordRequest, passwordDB);
    }

    public UserModel userByToken(String token){
        final String replaceToken = token.replace("Bearer ", "");
        final String userEmail = jwtService.extractUsername(replaceToken);
        Optional<UserModel> user = userRepository.findByEmail(userEmail);
        return user.get();
    }

    private UserModel updateData(UserModel userUpdate, UserUpdateRequest request){
        userUpdate.setBirthdate(this.convertStringToDateTime(request.getBirthDate()));
        userUpdate.setAt(request.getAt());
        userUpdate.setFirstname(request.getFirstname());
        userUpdate.setLastname(request.getLastname());
        return userUpdate;
    }

    private LocalDateTime convertStringToDateTime(String date){
        LocalDateTime eventDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        return eventDate;
    }

    private void validateValuesUpdate(UserUpdateRequest request){
        if(request.getFirstname() == null){
            throw new ObjectNotNullException("Firstname: Firstname is required.");
        }
        if(request.getLastname() == null){
            throw new ObjectNotNullException("Lastname: Lastname is required");
            
        }
        if(request.getAt() == null){
            throw new ObjectNotNullException("At: At is required");
            
        }
        return;
    }

    private void validateAtIsEqual(UserModel userUpdate, String requestAt){
        if(!userUpdate.getAt().equals(requestAt)){
            this.validateAtExists(requestAt);
        }
        return;
    }

    private void validateAtExists(String at){
        Optional<UserModel> obj = userRepository.findByAt(at);
        if(obj.isPresent()){
            throw new ObjectAlreadyExistsException("At: At already exists in the System.");
        }
        return;
        
    }
}
