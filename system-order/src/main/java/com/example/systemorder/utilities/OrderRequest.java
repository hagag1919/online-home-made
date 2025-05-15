package com.example.systemorder.utilities;

import java.util.List;

import com.example.systemorder.models.RequestDishs;

public class OrderRequest {
    private Long userID;
    private Long restaurantID;
    private List<RequestDishs> dishIDs;
    private String destination;
    private String shippingCompany;
    private Double totalPrice;

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
    public List<RequestDishs> getDishIDs() {
        return dishIDs;
    }
    public void setDishIDs(List<RequestDishs> dishIDs) {
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
    public Double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
    @Override
    public String toString() {
        return "OrderRequest{" +
                "userID=" + userID +
                ", restaurantID=" + restaurantID +
                ", dishIDs=" + dishIDs +
                ", destination='" + destination + '\'' +
                ", shippingCompany='" + shippingCompany + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }

}

