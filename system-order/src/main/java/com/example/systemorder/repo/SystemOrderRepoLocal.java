package com.example.systemorder.repo;

import com.example.systemorder.models.Order;
import com.example.systemorder.models.OrderDish;

import java.util.List;

public interface SystemOrderRepoLocal {
    void placeOrder(Order order);

    List<Order> getOrdersByUserId(Long userID);

    List<Order> getOrdersByRestaurantId(Long restaurantID);

    List<Order> getAllOrders();
    
    List<OrderDish> getOrderDishesByOrderId(Long orderId);
}
