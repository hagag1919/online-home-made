package com.example.systemadmin.utils;

import java.util.Objects;

public class Dish {

    private Long id;

    private String name;

    private double price;

    private int amount;

    private String description;

    private Long restaurant_id;

    public Dish() {
    }

    public Dish(String name, double price, int amount, String description, Long restaurant_id) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.description = description;
        this.restaurant_id = restaurant_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRestaurantId() {
        return restaurant_id;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurant_id = restaurantId;
    }




}