package com.example.system_restaurant.repo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.system_restaurant.models.Dish;


@Repository
public interface DishRepo extends JpaRepository<Dish, Long> {

    List<Dish> findByRestaurantId(Long restaurantId);
    boolean isAvailable(Long dishId, int amount);
    boolean updateAmount(Long dishId, int amount);
}