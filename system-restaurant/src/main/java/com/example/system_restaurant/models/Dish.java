package com.example.system_restaurant.models;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.ToString;

@Entity
@Table(name = "dishes")
@ToString
@NamedQueries({
    @NamedQuery(name = "Dish.findAll", query = "SELECT d FROM Dish d"),
    @NamedQuery(name = "Dish.findById", query = "SELECT d FROM Dish d WHERE d.id = :id"),
    @NamedQuery(name = "Dish.findByName", query = "SELECT d FROM Dish d WHERE d.name = :name"),
    @NamedQuery(name = "Dish.findByRestaurant", query = "SELECT d FROM Dish d WHERE d.restaurantId = :restaurantId"),
    @NamedQuery(name = "Dish.isAvailable", query = "SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Dish d WHERE d.id = :dishId AND d.amount >= :amount"),
    @NamedQuery(name = "Dish.updateAmount", query = "UPDATE Dish d SET d.amount = d.amount - :amount WHERE d.id = :dishId AND d.amount >= :amount"),
})
public class Dish implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "price", nullable = false)
    private double price;
    
    @Column(name = "amount", nullable = false)
    private int amount;
    
    @Column(name = "description", columnDefinition = "text")
    private String description;
    
    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;
    
    public Dish() {
    }
    
    public Dish(String name, double price, int amount, String description, Long restaurantId) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.description = description;
        this.restaurantId = restaurantId;
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
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
    
    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Objects.equals(id, dish.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
}