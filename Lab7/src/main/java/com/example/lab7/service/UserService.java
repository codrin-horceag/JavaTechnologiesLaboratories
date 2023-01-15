package com.example.lab7.service;

import com.example.lab7.model.User;

import javax.enterprise.event.Observes;


public interface UserService {
    String login(String username, String password);

    void addUser(@Observes User user);

}
