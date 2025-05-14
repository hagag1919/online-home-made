package com.example.systemorder.models;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userID;

    @Column(nullable = false)
    private Long restaurantID;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDishes> dishes;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private String shippingCompany;

    @Column(nullable = false)
    private String status;

    public Order(Long userID, Long restaurantID, List<OrderDishes> dishes,
                 BigDecimal totalPrice, String destination, String shippingCompany) {
        this.userID = userID;
        this.restaurantID = restaurantID;
        this.dishes = dishes;
        this.totalPrice = totalPrice;
        this.destination = destination;
        this.shippingCompany = shippingCompany;
        this.status = "Pending";
    }
    public Order() {
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
    public List<OrderDishes> getDishes() {
        return dishes;
    }
    public void setDishes(List<OrderDishes> dishes) {
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
