package com.example.user.models;

import lombok.Data;

@Data
public class Dish {
    
    private Long id;
    private String name;
    private String description;
    private double price;
    private Long restaurantId;

    public Dish() {
    }

    public Dish(Long id, String name, String description, double price, Long restaurantId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.restaurantId = restaurantId;
    }
}