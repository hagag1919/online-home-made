package com.example.systemorder.utilities;

import java.util.List;

import com.example.systemorder.models.Dish;

public class OrderRequest {
    private Long userID;
    private Long restaurantID;
    private List<Dish> dishIDs;
    private String destination;
    private String shippingCompany;

    // Getters and Setters
    public Long getUserID() {
        return userID;
    }
    public void setUserID(Long userID) {
        this.userID = userID;
    }
    public Long getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(Long restaurantID) {
        this.restaurantID = restaurantID;
    }
    public List<Dish> getDishIDs() {
        return dishIDs;
    }
    public void setDishIDs(List<Dish> dishIDs) {
        this.dishIDs = dishIDs;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getShippingCompany() {
        return shippingCompany;
    }
    public void setShippingCompany(String shippingCompany) {
        this.shippingCompany = shippingCompany;
    }
    @Override
    public String toString() {
        return "OrderRequest{" +
                "userID=" + userID +
                ", restaurantID=" + restaurantID +
                ", dishIDs=" + dishIDs +
                ", destination='" + destination + '\'' +
                ", shippingCompany='" + shippingCompany + '\'' +
                '}';
    }

}

