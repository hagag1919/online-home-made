package com.example.user.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.user.models.Restaurant;
import com.example.user.models.Dish;
import com.example.user.services.RestaurantService;


@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;


    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/getall")
    public ResponseEntity<?> getAllRestaurants() {
        try {
            List<Restaurant> restaurants = restaurantService.getAllRestaurants();
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching restaurants: " + e.getMessage());
        }
    }

    @GetMapping("/getbyrestaurantid")
    public ResponseEntity<?> getDishesByRestaurantId(@RequestParam Long restaurantId) {
        try {
            List<Dish> dishes = restaurantService.getDishesByRestaurantId(restaurantId);
            return ResponseEntity.ok(dishes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching dishes: " + e.getMessage());
        }
    }



    
}
