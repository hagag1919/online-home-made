package com.example.systemorder.services;

import com.example.systemorder.models.Dish;
import com.example.systemorder.models.Order;
import com.example.systemorder.models.RequestDishs;
import jakarta.ejb.Local;

import java.util.List;


@Local
public interface IOrderService {

    public void placeOrder(Long userID, Long restaurantID, List<RequestDishs> dishes, String destination, String shippingCompany);
    public List<Order> getAllOrdersByUserID(Long userID);
    public List<Order> getAllOrdersByRestaurantID(Long restaurantID);

    public List<Order> getAllOrders();


}
