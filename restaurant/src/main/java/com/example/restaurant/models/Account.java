package com.example.restaurant.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a restaurant account in the system.
 * Based on the simplified schema where accounts are restaurants.
 */
@Entity
@Table(name = "accounts")
public class Account implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Column(unique = true, nullable = false)
    private String name;
    
    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;
    
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dish> dishes = new ArrayList<>();
    
    // Default constructor
    public Account() {
    }
    
    // Constructor with required fields
    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }
    
    // Getters and Setters
    
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
    
    // Helper method to add a dish
    public void addDish(Dish dish) {
        dishes.add(dish);
        dish.setRestaurant(this);
    }
    
    // Helper method to remove a dish
    public void removeDish(Dish dish) {
        dishes.remove(dish);
        dish.setRestaurant(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id != null && id.equals(account.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
