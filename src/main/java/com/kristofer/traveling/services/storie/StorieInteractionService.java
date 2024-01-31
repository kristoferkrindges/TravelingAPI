package com.kristofer.traveling.services.storie;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.models.StorieModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.StorieRepository;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;
import com.kristofer.traveling.services.exceptions.ObjectNotPermission;
import com.kristofer.traveling.services.like.LikeService;
import com.kristofer.traveling.services.users.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorieInteractionService {
    private final StorieRepository storieRepository;
    private final UserService userService;
    private final LikeService likeService;

    public String delete(String token, UUID id){
        StorieModel storieModel = this.findStorie(id);
        this.verifyIdUser(token, storieModel.getCreator().getId());
        likeService.deleteAllLikesStories(storieModel);
        this.storieRepository.delete(storieModel);
        return "Delete with sucess!";
    }

    private void verifyIdUser(String token, UUID id){
        UserModel user = userService.userByToken(token);
        if(user.getId() != id){
            throw new ObjectNotPermission("This user does not have permission to change Post");
        }
    }

    private StorieModel findStorie(UUID id){
        Optional<StorieModel> storieModel = storieRepository.findById(id);
        if(!storieModel.isPresent()){
            throw new ObjectNotFoundException("Post with id " + id + " not found");
        }
        return storieModel.get();
    }
}
