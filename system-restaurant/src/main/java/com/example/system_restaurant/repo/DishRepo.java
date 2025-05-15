package com.example.system_restaurant.repo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.system_restaurant.models.Dish;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DishRepo extends JpaRepository<Dish, Long> {

    List<Dish> findByRestaurantId(Long restaurantId);

    @Query(name = "Dish.isAvailable")
    boolean isAvailable(Long dishId, int amount);

    @Modifying
    @Transactional
    @Query(name = "Dish.updateAmount")
    int updateAmount(Long dishId, int amount);
}
