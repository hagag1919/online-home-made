package com.example.restaurant.repo;

import com.example.restaurant.models.Account;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

/**
 * Repository for Account entity operations.
 */
@Stateless
public class AccountRepository extends AbstractRepository<Account, Long> {
    
    public AccountRepository() {
        super(Account.class);
    }
    
    /**
     * Find an account by name.
     *
     * @param name the name to search for
     * @return the account wrapped in an Optional, or an empty Optional if not found
     */
    public Optional<Account> findByName(String name) {
        try {
            TypedQuery<Account> query = entityManager.createQuery(
                "SELECT a FROM Account a WHERE a.name = :name", Account.class);
            query.setParameter("name", name);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Check if a name already exists.
     *
     * @param name the name to check
     * @return true if the name exists, false otherwise
     */
    public boolean existsByName(String name) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(a) FROM Account a WHERE a.name = :name", Long.class);
        query.setParameter("name", name);
        return query.getSingleResult() > 0;
    }
    
    /**
     * Authenticate an account by name and password.
     *
     * @param name the name
     * @param password the password
     * @return the account wrapped in an Optional, or an empty Optional if authentication fails
     */
    public Optional<Account> authenticate(String name, String password) {
        try {
            TypedQuery<Account> query = entityManager.createQuery(
                "SELECT a FROM Account a WHERE a.name = :name AND a.password = :password", Account.class);
            query.setParameter("name", name);
            query.setParameter("password", password);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
