package com.example.systemorder.services;

import com.example.systemorder.models.Order;
import com.example.systemorder.models.OrderDishes;
import com.example.systemorder.repo.SystemOrderRepo;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Stateless
public class OrderService implements IOrderService{

    @EJB
    private SystemOrderRepo systemOrderRepo;

    @Override
    public void placeOrder(Long userID, Long restaurantID, List<Long> dishIDs, String destination, String shippingCompany) {
        Order order = new Order();
        order.setUserID(userID);
        order.setRestaurantID(restaurantID);
        order.setDestination(destination);
        order.setShippingCompany(shippingCompany);
        order.setStatus("PENDING");
        order.setDishes(new ArrayList<>());
        order.setTotalPrice(new BigDecimal(0));
        for (Long dishID : dishIDs) {
            OrderDishes orderDish = new OrderDishes();
            orderDish.setId(dishID);
            orderDish.setOrder(order);
            order.getDishes().add(orderDish);
        }
        systemOrderRepo.saveOrder(order);
    }

    @Override
    public List<Order> getAllOrdersByUserID(Long userID) {
        return systemOrderRepo.getOrdersByUserId(userID);
    }


    @Override
    public List<Order> getAllOrdersByRestaurantID(Long restaurantID) {
        return systemOrderRepo.getOrdersByRestaurantId(restaurantID);
    }

    @Override
    public List<Order> getAllOrders() {
        return  systemOrderRepo.getAllOrders();
    }
}
