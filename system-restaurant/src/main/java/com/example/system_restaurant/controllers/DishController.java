package com.example.system_restaurant.controllers;
import com.example.system_restaurant.models.Dish;
import com.example.system_restaurant.repo.DishRepo;
import com.example.system_restaurant.services.DishService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/dishes")
@CrossOrigin(origins = "*")
public class DishController {

    private final DishRepo dishRepo;
    private final DishService dishService;

    public DishController(DishService dishService, DishRepo dishRepo) {
        this.dishService = dishService;
        this.dishRepo = dishRepo;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createDish(@RequestBody Dish dish, @RequestParam Long restaurantId) {
        dish.setRestaurantId(restaurantId);
        if (dish.getName() == null || dish.getPrice() == 0 || dish.getDescription() == null) {
            return ResponseEntity.badRequest().body("Invalid dish data");
        }

        return dishService.createDish(dish);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateDish(@RequestParam Long restaurantId,@RequestBody Dish dish, @RequestParam Long dishId) {
        return dishService.updateDish(restaurantId, dish, dishId);
    }

    @PostMapping("/getbyids")
    public ResponseEntity<List<Dish>> getDishesByIds(@RequestBody List<Long> ids) {
        List<Dish> dishes = dishRepo.findAllById(ids);
        if (dishes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("/getbyrestaurantid")
    public ResponseEntity<List<Dish>> getDishesByRestaurantId(@RequestParam Long restaurantId) {
        List<Dish> dishes = dishRepo.findByRestaurantId(restaurantId);
        if (dishes.isEmpty()) {
            // return that there are no dishes not 404
            return ResponseEntity.ok().body(List.of());
        }
        return ResponseEntity.ok(dishes);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDish(@RequestParam Long DishId, @RequestParam Long restaurantId) {
        return dishService.deleteDish(DishId , restaurantId);
    }
        
}