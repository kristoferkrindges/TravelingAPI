package com.kristofer.traveling.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kristofer.traveling.models.ConfigurationModel;
import com.kristofer.traveling.models.UserModel;
import com.kristofer.traveling.repositories.ConfigurationRepository;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfigurationService {
    private final ConfigurationRepository configurationRepository;

    public ConfigurationModel creteConfiguration(UserModel user){
        return configurationRepository.save(this.createDataConfiguration(user));
    }

    public void toogleDarkMode(Long userId){
        Optional<ConfigurationModel> optionalConfiguration = configurationRepository.findByUserId(userId);
        if(optionalConfiguration.get().isDarkMode()){
            optionalConfiguration.get().setDarkMode(false);
        }else{
            optionalConfiguration.get().setDarkMode(true);
        }
        configurationRepository.save(optionalConfiguration.get());
    }

    public ConfigurationModel getAllConfigurationsUser(Long userId){
        return configurationRepository.findByUserId(userId)
            .orElseThrow(()-> new ObjectNotFoundException("Configuration not found!"));
    }

    public void deletConfigurationUser(Long userId){
        Optional<ConfigurationModel> configuration = configurationRepository.findByUserId(userId);
        if(configuration.isPresent()){
            configurationRepository.deleteById(configuration.get().getId());
        }
    }

    private ConfigurationModel createDataConfiguration(UserModel user){
        var newConfiguration = ConfigurationModel.builder()
                .user(user)
                .darkMode(false)
                .build();
        return newConfiguration;
    }
}
