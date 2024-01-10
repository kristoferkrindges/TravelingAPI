package com.kristofer.traveling.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.dtos.requests.storie.StorieRequest;
import com.kristofer.traveling.dtos.responses.post.PostAllResponse;
import com.kristofer.traveling.dtos.responses.storie.StorieAllResponse;
import com.kristofer.traveling.dtos.responses.user.UserAllResponse;
import com.kristofer.traveling.models.CommentModel;
import com.kristofer.traveling.models.PostModel;
import com.kristofer.traveling.models.StorieModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.models.Enums.NotificationTypeEnum;
import com.kristofer.traveling.repositories.StorieRepository;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;
import com.kristofer.traveling.services.exceptions.ObjectNotNullException;
import com.kristofer.traveling.services.exceptions.ObjectNotPermission;
import com.kristofer.traveling.services.users.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorieService {
    private final StorieRepository storieRepository;
    private final UserService userService;
    private final LikeService likeService;
    private final NotificationService notificationService;

    public List<StorieAllResponse> findAll(){
        List<StorieModel> stories = storieRepository.findAll();
        List<StorieAllResponse> storieAllResponse = stories.stream().map(x-> new StorieAllResponse(x))
        .collect(Collectors.toList());
        return storieAllResponse;
    }

    public List<StorieAllResponse> getStoriesByUserOrderedByDate(String at, String token) {
        UserModel user = userService.findByAt(at);
        List<StorieModel> stories = storieRepository.findByCreatorOrderByDatePublicDesc(user);
        List<StorieAllResponse> storieAllResponse = stories.stream().map(x-> new StorieAllResponse(
            x, this.pressLike(token, x)))
        .collect(Collectors.toList());
        return storieAllResponse;
    }

    public List<UserAllResponse> getUsersWithStoriesOrderedByLatestStory() {
        List<UserModel> users = storieRepository.findAllUsersWithStoriesOrderByLatestStory();
        List<UserAllResponse> usersAllResponse = users.stream().map(x-> new UserAllResponse(x))
        .collect(Collectors.toList());
        return usersAllResponse;
    }

    public void toogleLikeStorie(String token, Long storieId){
        UserModel user = userService.userByToken(token);
        StorieModel storie = this.verifyStorieExistId(storieId);
        Boolean verify = likeService.toggleLikeStorie(user, storie);
        if(verify){
            notificationService.createNotification(storie.getCreator(), NotificationTypeEnum.LIKESTORIE, user, storie.getId());
        }
    }

    public StorieAllResponse findById(Long id){
        StorieModel storieModel = this.findStorie(id);
        StorieAllResponse storie = new StorieAllResponse(storieModel);
        return storie;
    }

    public UserAllResponse insert(String token, StorieRequest request){
        UserModel user = userService.userByToken(token);
        StorieModel storie = this.createdStorieModel(user, request);
        storieRepository.save(storie);
        return new UserAllResponse(user);
    }

    public StorieAllResponse update(String token, StorieRequest request, Long id){
        StorieModel storie = this.updateDataStorie(token, request, id);
        storieRepository.save(storie);
        return new StorieAllResponse(storie);
    }

    public String delete(String token, Long id){
        StorieModel storieModel = this.findStorie(id);
        this.verifyIdUser(token, storieModel.getCreator().getId());
        this.storieRepository.delete(storieModel);
        return "Delete with sucess!";
    }

    public boolean pressLike(String token, StorieModel storie){
        UserModel user = userService.userByToken(token);
        return likeService.pressLikeStorie(user, storie);
    }

    private StorieModel updateDataStorie(String token, StorieRequest request, Long id){
        StorieModel storieModel = this.verifyStorieExistId(id);
        this.verifyRequestUpdate(request);
        this.verifyIdUser(token, request.getCreatorId());
        storieModel.setVideo(request.getVideo());
        storieModel.setDatePublic(request.getDatePublic());
        return storieModel;
    }

    private void verifyIdUser(String token, Long id){
        UserModel user = userService.userByToken(token);
        if(user.getId() != id){
            throw new ObjectNotPermission("This user does not have permission to change Storie");
        }
    }

    private StorieModel findStorie(Long id){
        Optional<StorieModel> storieModel = storieRepository.findById(id);
        if(!storieModel.isPresent()){
            throw new ObjectNotFoundException("Storie with id " + id + " not found");
        }
        return storieModel.get();
    }

    private StorieModel verifyStorieExistId(Long id){
        Optional<StorieModel> storie = storieRepository.findById(id);
        return storie.orElseThrow(
            ()-> new ObjectNotFoundException("Storie with id " + id + " not found"));
    }

    private StorieModel createdStorieModel(UserModel user, StorieRequest request){
        this.verifyRequestInsert(request);
        var storie = StorieModel.builder()
            .video(request.getVideo())
            .datePublic(request.getDatePublic())
            .creator(user)
            .build();
        return storie;
    }

    private void verifyRequestInsert(StorieRequest request){
        if(request.getVideo() == null){
            throw new ObjectNotNullException("Video: Video is required.");
        }
        if(request.getDatePublic() == null){
            throw new ObjectNotNullException("DatePublic: DatePublic is required.");
        }
    }

    private void verifyRequestUpdate(StorieRequest request){
        if(request.getVideo() == null){
            throw new ObjectNotNullException("Video: Video is required.");
        }
        if(request.getDatePublic() == null){
            throw new ObjectNotNullException("DatePublic: DatePublic is required.");
        }
        if(request.getCreatorId() == null){
            throw new ObjectNotNullException("Creator ID: Creator ID is required.");
        }
    }
}
