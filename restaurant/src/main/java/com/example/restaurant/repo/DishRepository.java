package com.example.restaurant.repo;

import com.example.restaurant.models.Dish;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Repository for Dish entity operations.
 */
@Stateless
public class DishRepository extends AbstractRepository<Dish, Long> {
    
    public DishRepository() {
        super(Dish.class);
    }
    
    /**
     * Find dishes by restaurant ID.
     *
     * @param restaurantId the ID of the restaurant (account)
     * @return a list of dishes from the specified restaurant
     */
    public List<Dish> findByRestaurantId(Long restaurantId) {
        TypedQuery<Dish> query = entityManager.createQuery(
            "SELECT d FROM Dish d WHERE d.restaurant.id = :restaurantId", Dish.class);
        query.setParameter("restaurantId", restaurantId);
        return query.getResultList();
    }
    
    /**
     * Search dishes by name or description within a restaurant.
     *
     * @param restaurantId the ID of the restaurant (account)
     * @param keyword the keyword to search for
     * @return a list of dishes matching the search criteria
     */
    public List<Dish> searchByRestaurantId(Long restaurantId, String keyword) {
        String searchPattern = "%" + keyword.toLowerCase() + "%";
        TypedQuery<Dish> query = entityManager.createQuery(
            "SELECT d FROM Dish d WHERE d.restaurant.id = :restaurantId AND " +
            "(LOWER(d.name) LIKE :pattern OR LOWER(d.description) LIKE :pattern)", 
            Dish.class);
        query.setParameter("restaurantId", restaurantId);
        query.setParameter("pattern", searchPattern);
        return query.getResultList();
    }
}
