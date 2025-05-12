package com.example.system_restaurant.repo;
import com.example.system_restaurant.models.Dish;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DishRepo extends JpaRepository<Dish, Long> {

    List<Dish> findByRestaurantId(Long restaurantId);
}