package com.example.lab7.service;

import com.example.lab7.annotations.RegistrationNumber;
import com.example.lab7.model.Documents;
import com.example.lab7.repository.DocumentsRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

public class DocumentsService implements Serializable {
    @Inject @RegistrationNumber
    private String registrationNumber;

    @Inject
    private DocumentsRepository documentsRepository;

    public void addDocument(String author, String title){
        documentsRepository.saveDocument(new Documents(
                author,
                title,
                registrationNumber
        ));
    }

    public List<Documents> getDocuments(){
        return documentsRepository.getDocuments();
    }
}
