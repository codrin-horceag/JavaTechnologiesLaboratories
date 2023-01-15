package com.example.lab7.service.implementation;

import com.example.lab7.model.User;
import com.example.lab7.repository.UserRepository;
import com.example.lab7.service.UserService;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.Serializable;

public class UserServiceImpl implements UserService, Serializable {
    @Inject
    private UserRepository userRepository;

    public String login(String username, String password){
        try{
            return userRepository.login(username, password);
        }catch(RuntimeException error){
            return error.getMessage();
        }
    }

    public void addUser(@Observes User user){
        userRepository.saveUser(user);
    }


}
