package com.example.systemorder.services;

import java.util.List;

import com.example.systemorder.models.Order;
import com.example.systemorder.models.RequestDishs;

import jakarta.ejb.Local;


@Local
public interface IOrderService {

    public void placeOrder(Long userID, Long restaurantID, List<RequestDishs> dishes, String destination, String shippingCompany,Double totalPrice);
    public List<Order> getAllOrdersByUserID(Long userID);
    public List<Order> getAllOrdersByRestaurantID(Long restaurantID);

    public List<Order> getAllOrders();


}
