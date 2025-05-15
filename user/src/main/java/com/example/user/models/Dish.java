package com.example.user.models;

import lombok.Data;

@Data
public class Dish {
    
    private Long id;
    private String name;
    private String description;
    private double price;
    private Long restaurantId;
    private int amount;

    public Dish() {
    }

    public Dish(Long id, String name, String description, double price, Long restaurantId, int amount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.restaurantId = restaurantId;
        this.amount = amount;
    }
}