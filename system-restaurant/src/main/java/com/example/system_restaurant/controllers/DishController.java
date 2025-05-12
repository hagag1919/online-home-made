package com.example.system_restaurant.controllers;
import com.example.system_restaurant.models.Dish;
import com.example.system_restaurant.repo.DishRepo;
import com.example.system_restaurant.services.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishRepo dishRepo;
    private final DishService dishService;

    public DishController(DishService dishService, DishRepo dishRepo) {
        this.dishService = dishService;
        this.dishRepo = dishRepo;
    }


    public ResponseEntity<List<Dish>> getDishesByRestaurantId(Long restaurantId) {
        return dishService.getDishesByRestaurantId(restaurantId);
    }

    @PostMapping("/create/{restaurantId}")
    public ResponseEntity<String> createDish(@RequestBody Dish dish, @PathVariable Long restaurantId) {
        dish.setRestaurantId(restaurantId);
        if (dish.getName() == null || dish.getPrice() == 0 || dish.getDescription() == null) {
            return ResponseEntity.badRequest().body("Invalid dish data");
        }

        return dishService.createDish(dish);
    }

    @PutMapping("/update/{restaurantId}/{id}")
    public ResponseEntity<String> updateDish(@PathVariable Long restaurantId,@RequestBody Dish dish, @PathVariable Long id) {
        return dishService.updateDish(restaurantId, dish, id);
    }

    @GetMapping("/getbyids")
    public ResponseEntity<List<Dish>> getDishesByIds(@RequestBody List<Long> ids) {
        List<Dish> dishes = dishRepo.findAllById(ids);
        if (dishes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dishes);
    }
        
}