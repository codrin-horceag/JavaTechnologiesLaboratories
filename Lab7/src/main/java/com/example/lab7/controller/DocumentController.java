package com.example.lab7.controller;

import com.example.lab7.annotations.Loggable;
import com.example.lab7.annotations.RegistrationNumber;
import com.example.lab7.model.Documents;
import com.example.lab7.model.User;
import com.example.lab7.repository.DocumentsRepository;
import com.example.lab7.repository.UserRepository;


import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.Subject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Named("DocumentController")
@ApplicationScoped
@Transactional
public class DocumentController {
    @Inject
    private DocumentsRepository documentRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private Subject subject;

    @Inject
    @RegistrationNumber
    private String generatedRegistrationNumber;

    private String documentContent;
    private String documentName;
    private String additionalAuthors;
    private int documentId;

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    @Loggable
    public void addDocument() throws IOException {
        if (documentContent == null || documentContent.length() < 1 || documentName == null || documentName.length() < 1) {
            System.err.println("Invalid Document!");
            return;
        }
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        String username = null;
        String[] authorsString = additionalAuthors.split(",");
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("currentUsername")) {
                    username = cookie.getValue();
                }
            }
        }
        List<String> authors = new ArrayList<>();
        Optional<User> optUser = userRepository.getByUsername(username);
        if (optUser.isPresent()) {
            authors.add(username);
            if (optUser.get().getRole().equals("AUTHOR")) {
                System.err.println("You are not logged in or you are not an Author!");
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
                return;
            }
            for (String authorUsername : authorsString) {
                if (userRepository.getByUsername(authorUsername).isPresent()) {
                    authors.add(authorUsername);
                }
                documentRepository.saveDocument(new Documents(String.join(",", authors), documentContent, generatedRegistrationNumber));
                FacesContext.getCurrentInstance().getExternalContext().redirect("author.xhtml");
            }
        } else {
            System.err.println("You are not logged in or you are not an Author!");
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        }
    }

    public void deleteDocument(int id) throws IOException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        String username = null;
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("currentUsername")) {
                    username = cookie.getValue();
                }
            }
        }
        if (username != null && username.length() > 0) {
            Optional<User> optUser = userRepository.getByUsername(username);
            if (optUser.isPresent() && optUser.get().getRole().equals("REVIEWER")) {
                documentRepository.delete(id);
            } else {
                System.err.println("No permissions to do such thing!");
            }
        } else {
            System.err.println("Log in before doing so!");
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            return;
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("reviewer.xhtml");
    }

    public void updateDocument() throws IOException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        String username = null;
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("currentUsername")) {
                    username = cookie.getValue();
                }
            }
        }
        if (username != null && username.length() > 0) {
            Optional<User> optUser = userRepository.getByUsername(username);
            if (optUser.isPresent() && optUser.get().getRole().equals("REVIEWER")) {
                documentRepository.update(documentName, documentContent, documentId);
            } else {
                System.err.println("No permissions to do such thing!");
            }
        } else {
            System.err.println("Log in before doing so!");
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            return;
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("reviewer.xhtml");
    }

    public List<Documents> getDocuments() {
        return documentRepository.getDocuments();
    }

    public String getAdditionalAuthors() {
        return additionalAuthors;
    }

    public void setAdditionalAuthors(String additionalAuthors) {
        this.additionalAuthors = additionalAuthors;
    }

    public String getDocumentContent() {
        return documentContent;
    }

    public void setDocumentContent(String documentContent) {
        this.documentContent = documentContent;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}
