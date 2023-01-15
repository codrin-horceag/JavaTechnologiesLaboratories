package com.example.lab7.controller;



import com.example.lab7.model.User;
import com.example.lab7.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Named("UserController")
@ApplicationScoped
@Transactional
public class UserController {
    @Inject
    private UserRepository userRepository;
    private String username;
    private String password;
    private String email;
    private String role;

    public void login() throws IOException {
        User user = userRepository.getByUsername(username).orElse(null);
        if (user == null || !user.getPassword().equals(password)) {
            System.err.println("User not found or wrong password!");
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            return;
        }
        FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("currentUsername", user.getUsername(), new HashMap<>());
        if (user.getRole().equals("ADMIN"))
            FacesContext.getCurrentInstance().getExternalContext().redirect("admin.xhtml");
        else {
            if (user.getRole().equals("AUTHOR")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("author.xhtml");
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("reviewer.xhtml");
            }
        }
    }

    public String register() {
        if (username == null || username.length() < 1 || email == null || email.length() < 1) {
            System.err.println("Username or email not valid!");
        } else {
            if (!userRepository.getByUsername(username).isPresent() && !userRepository.getByEmail(email).isPresent())
                userRepository.saveUser(new User(username, password, role));
            else {
                System.err.println("Username or password already exists!");
            }
        }
        return "/login.xhtml?faces-redirect=true";
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void goToRegister() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("register.xhtml");
    }

    public void goToLogin() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
