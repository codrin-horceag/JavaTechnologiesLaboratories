package com.example.lab7.repository;

import com.example.lab7.model.Documents;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Stateless
@Transactional
public class DocumentsRepository implements Serializable {
    @PersistenceContext(unitName = "lab7PU")
    EntityManager entityManager;

    public List<Documents> getDocuments(){
        return entityManager.createNamedQuery("Documents.getDocuments", Documents.class).getResultList();
    }
    public void saveDocument(Documents document){
        entityManager.persist(document);
    }

    public void delete(int id) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.createNativeQuery("delete from documents where id=(?)").setParameter(1, id).executeUpdate();
        entityTransaction.commit();
    }

    public void update(String documentName, String documentContent, int oldId) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        if (documentName != null && documentName.length() > 0) {
            entityManager.createNativeQuery("update documents set name=(?) where id=(?)").setParameter(1, documentName).setParameter(2, oldId).executeUpdate();
        }
        if (documentContent != null && documentContent.length() > 0) {
            entityManager.createNativeQuery("update documents set content=(?) where id=(?)").setParameter(1, documentContent).setParameter(2, oldId).executeUpdate();
        }
        entityTransaction.commit();
    }
}
