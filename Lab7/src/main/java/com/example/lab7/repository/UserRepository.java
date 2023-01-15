package com.example.lab7.repository;

import com.example.lab7.model.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.Optional;

@Stateless
@Transactional
public class UserRepository{
    @PersistenceContext(unitName = "lab7PU")
    EntityManager entityManager;
    public void saveUser(User user){
        entityManager.persist(user);
    }
    public String login(String username, String password){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> playerRoot = criteriaQuery.from(User.class);
        Predicate predicateForUsername = criteriaBuilder.equal(playerRoot.get("username"), username);
        Predicate predicateForPassword = criteriaBuilder.equal(playerRoot.get("password"), password);
        Predicate finalPredicate = criteriaBuilder.and(predicateForUsername, predicateForPassword);
        criteriaQuery.where(finalPredicate);
        Query query = entityManager.createQuery(criteriaQuery);
        @SuppressWarnings("unchecked")
        User user = (User) query.getSingleResult();
        return user.getRole();
    }

    public Optional<User> getByUsername(String username) {
        Object obj;
        try {
            obj = entityManager.createNativeQuery("SELECT * FROM users WHERE username = (?)", User.class).setParameter(1, username).getSingleResult();
        } catch (NoResultException ex) {
            return Optional.empty();
        }
        return Optional.of((User) obj);
    }

    public Optional<User> getByEmail(String email) {
        Object obj;
        try {
            obj = entityManager.createNativeQuery("SELECT * FROM users WHERE email = (?)", User.class).setParameter(1, email).getSingleResult();
        } catch (NoResultException ex) {
            return Optional.empty();
        }
        return Optional.of((User) obj);
    }
}
