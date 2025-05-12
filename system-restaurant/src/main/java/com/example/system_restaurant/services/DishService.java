package com.example.system_restaurant.services;
import com.example.system_restaurant.models.Dish;
import com.example.system_restaurant.repo.DishRepo;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DishService {
    private final DishRepo dishRepo;

    public DishService(DishRepo dishRepo) {
        this.dishRepo = dishRepo;
    }

    public Dish findById(Long id) {
        return dishRepo.findById(id).orElse(null);
    }
    public void save(Dish dish) {
        dishRepo.save(dish);
    }
    public void delete(Long id) {
        dishRepo.deleteById(id);
    }
    public List<Dish> findAll() {
        return dishRepo.findAll();
    }
    public List<Dish> findByRestaurantId(Long restaurantId) {
        return dishRepo.findByRestaurantId(restaurantId);
    }

    public ResponseEntity<List<Dish>> getDishesByRestaurantId(Long restaurantId) {
        List<Dish> dishes = dishRepo.findByRestaurantId(restaurantId);
        if (dishes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dishes);
    }

    public ResponseEntity<String> createDish(Dish dish) {
        try {
            // No need to manually set ID as it's auto-generated
            dishRepo.save(dish);
            return ResponseEntity.status(201).body("Dish created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating dish: " + e.getMessage());
        }
    }

    public ResponseEntity<String> updateDish(Long restaurantId, Dish dish, Long id) {
        Dish existingDish = dishRepo.findById(id).orElse(null);
        if (existingDish == null) {
            return ResponseEntity.notFound().build();
        }
        if(existingDish.getRestaurantId() != restaurantId) {
            return ResponseEntity.status(403).body("You do not have permission to update this dish");
        }
        existingDish.setName(dish.getName());
        existingDish.setPrice(dish.getPrice());
        existingDish.setDescription(dish.getDescription());
        dishRepo.save(existingDish);
        return ResponseEntity.ok("Dish updated successfully");
    }
}