package com.example.systemorder.services;

import com.example.systemorder.models.Order;

import java.util.List;

public interface IOrderService {

    //PlaceOrder
    //getAllOrdersByUserID
    //getAllOrdersByRestaurantID

    public void placeOrder(Long userID, Long restaurantID, List<Long> dishIDs, String destination, String shippingCompany);
    public List<Order> getAllOrdersByUserID(Long userID);
    public List<Order> getAllOrdersByRestaurantID(Long restaurantID);

}
