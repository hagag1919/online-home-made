package com.example.systemorder.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userID;

    @Column(nullable = false)
    private Long restaurantID;

    @ElementCollection
    @CollectionTable(name = "order_dishes_link", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "dish_id")
    private List<Long> dishes;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private String shippingCompany;

    @Column(nullable = false)
    private String status = "Pending";

    // Constructors
    public Order() {
    }

    public Order(Long userID, Long restaurantID, List<Long> dishes,
                 BigDecimal totalPrice, String destination, String shippingCompany) {
        this.userID = userID;
        this.restaurantID = restaurantID;
        this.dishes = dishes != null ? dishes : new ArrayList<>();
        this.totalPrice = totalPrice;
        this.destination = destination;
        this.shippingCompany = shippingCompany;
        this.status = "Pending";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<Long> getDishes() {
        return dishes;
    }

    public void setDishes(List<Long> dishes) {
        this.dishes = dishes;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}