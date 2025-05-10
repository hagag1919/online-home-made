package com.example.systemorder.repo;

import com.example.systemorder.models.Order;
import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Singleton
public class SystemOrderRepo {
    @PersistenceContext(unitName = "systemorderPU")
    private EntityManager entityManager;

    public void saveOrder(Order order) {
        entityManager.persist(order);
    }

    public Order findOrderById(Long id) {
        return entityManager.find(Order.class, id);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        TypedQuery<Order> query = entityManager.createQuery(
                "SELECT o FROM Order o WHERE o.userID = :userId", Order.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<Order> getOrdersByRestaurantId(Long restaurantId) {
        TypedQuery<Order> query = entityManager.createQuery(
                "SELECT o FROM Order o WHERE o.restaurantID = :restaurantId", Order.class);
        query.setParameter("restaurantId", restaurantId);
        return query.getResultList();
    }

    public void updateOrder(Order order) {
        entityManager.merge(order);
    }

    public void deleteOrder(Order order) {
        if (!entityManager.contains(order)) {
            order = entityManager.merge(order);
        }
        entityManager.remove(order);
    }

    // get all orders
    public List<Order> getAllOrders() {
        TypedQuery<Order> query = entityManager.createQuery(
                "SELECT o FROM Order o", Order.class);
        return query.getResultList();
    }



}
