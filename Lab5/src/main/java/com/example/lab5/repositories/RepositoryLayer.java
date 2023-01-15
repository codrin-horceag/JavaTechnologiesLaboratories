package com.example.lab5.repositories;

import com.example.lab5.entities.City;
import com.example.lab5.entities.Team;

import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ManagedBean
@RequestScoped
@Transactional
public class RepositoryLayer {
    @PersistenceContext(unitName = "lab-5")
    EntityManager entityManager;

    public void insertIntoDB(String teamName, String teamFoundationDate, String city) {
        City cityToInsert = new City(UUID.randomUUID().toString(), city);
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(cityToInsert);

        Team teamToBeInserted = new Team(UUID.randomUUID().toString(), city, teamFoundationDate, teamName);
        entityManager.persist(teamToBeInserted);

        entityTransaction.commit();
    }

    public void updateFromDB(String teamName, String teamFoundationDate, String city) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        List<City> existentCity = entityManager.createNativeQuery("SELECT * from cities where name=(?)", City.class).setParameter(1, city).getResultList();
        if (existentCity.size() == 0) {
            City cityToInsert = new City(UUID.randomUUID().toString(), city);
            entityManager.persist(cityToInsert);
        }

        entityManager.createNamedQuery("updateTeamCity").setParameter(1, city).setParameter(2, teamName).executeUpdate();

        entityManager.createNamedQuery("updateTeamFounding").setParameter(1, teamFoundationDate).setParameter(2, teamName).executeUpdate();
        entityTransaction.commit();
    }

    public void deleteFromDB(String cityName) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.createNamedQuery("deleteTeam").setParameter(1, cityName).executeUpdate();

        entityManager.createNamedQuery("deleteCity").setParameter(1, cityName).executeUpdate();
        entityTransaction.commit();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Team> fetchTeamsFromDB() {
        return entityManager.createNativeQuery("SELECT * FROM teams", Team.class).getResultList();
    }
}
