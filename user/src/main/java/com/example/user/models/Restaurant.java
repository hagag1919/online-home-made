package com.example.user.models;

import lombok.Data;

@Data
public class Restaurant {

    private Long id;
    private String name;
    

    public Restaurant() {
    }
    public Restaurant(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
