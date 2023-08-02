package com.kristofer.traveling.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.controllers.user.requests.StorieRequest;
import com.kristofer.traveling.controllers.user.responses.StorieAllResponse;
import com.kristofer.traveling.models.StorieModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.StorieRepository;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;
import com.kristofer.traveling.services.exceptions.ObjectNotNullException;
import com.kristofer.traveling.services.exceptions.ObjectNotPermission;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorieService {
    private final StorieRepository storieRepository;
    private final UserService userService;

    public List<StorieAllResponse> findAll(){
        List<StorieModel> stories = storieRepository.findAll();
        List<StorieAllResponse> storieAllResponse = stories.stream().map(x-> new StorieAllResponse(x))
        .collect(Collectors.toList());
        return storieAllResponse;
    }

    public StorieAllResponse findById(Long id){
        StorieModel storieModel = this.findStorie(id);
        StorieAllResponse storie = new StorieAllResponse(storieModel);
        return storie;
    }

    public StorieAllResponse insert(String token, StorieRequest request){
        StorieModel storie = this.createdStorieModel(token, request);
        storieRepository.save(storie);
        return new StorieAllResponse(storie);
    }

    public void update(String token, StorieRequest request, Long id){
        StorieModel storie = this.updateDataStorie(token, request, id);
        storieRepository.save(storie);
    }

    public void delete(String token, Long id){
        StorieModel storieModel = this.findStorie(id);
        this.verifyIdUser(token, id);
        this.storieRepository.delete(storieModel);
    }

    private StorieModel updateDataStorie(String token, StorieRequest request, Long id){
        this.verifyRequestUpdate(request);
        this.verifyIdUser(token, request.getCretorId());
        Optional<StorieModel> storieModel = storieRepository.findById(id);
        storieModel.get().setVideo(request.getVideo());
        storieModel.get().setDatePublic(request.getDatePublic());
        return storieModel.get(); 
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

    private StorieModel createdStorieModel(String token, StorieRequest request){
        this.verifyRequestInsert(request);
        UserModel user = userService.userByToken(token);
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
        if(request.getCretorId() == null){
            throw new ObjectNotNullException("Creator ID: Creator ID is required.");
        }
    }
}
