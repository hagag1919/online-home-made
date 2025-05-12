package com.example.restaurant.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * A generic repository interface for database operations.
 *
 * @param <T> the entity type
 * @param <ID> the type of the entity's ID
 */
public abstract class AbstractRepository<T, ID> {
    
    @PersistenceContext(unitName = "restaurantPU")
    protected EntityManager entityManager;
    
    private final Class<T> entityClass;
    
    protected AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    /**
     * Find an entity by its ID.
     *
     * @param id the ID of the entity
     * @return the entity wrapped in an Optional, or an empty Optional if not found
     */
    public Optional<T> findById(ID id) {
        T entity = entityManager.find(entityClass, id);
        return Optional.ofNullable(entity);
    }
    
    /**
     * Find all entities of this type.
     *
     * @return a list of all entities
     */
    public List<T> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        TypedQuery<T> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
    
    /**
     * Save an entity (create or update).
     *
     * @param entity the entity to save
     * @return the saved entity
     */
    @Transactional
    public T save(T entity) {
        return entityManager.merge(entity);
    }
    
    /**
     * Delete an entity by its ID.
     *
     * @param id the ID of the entity to delete
     */
    @Transactional
    public void deleteById(ID id) {
        findById(id).ifPresent(entity -> entityManager.remove(entity));
    }
    
    /**
     * Count the number of entities.
     *
     * @return the count of entities
     */
    public long count() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(entityClass)));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
