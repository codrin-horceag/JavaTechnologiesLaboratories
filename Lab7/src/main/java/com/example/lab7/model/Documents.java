package com.example.lab7.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "documents")
@NamedQueries({
        @NamedQuery(name = "Documents.getDocuments", query = "select d from Documents d")
})
@Getter
@Setter
@NoArgsConstructor
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String author;
    private String title;
    private String registration_number;

    public Documents(String author, String title, String registration_number) {
        this.author = author;
        this.title = title;
        this.registration_number = registration_number;
    }

}
