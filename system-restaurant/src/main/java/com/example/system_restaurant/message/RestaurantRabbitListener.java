package com.example.system_restaurant.message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.system_restaurant.repo.AccountRepo;
import com.example.system_restaurant.repo.DishRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class RestaurantRabbitListener {
    private final AccountRepo accountRepo;
    private final DishRepo dishRepo;
    @Autowired
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public RestaurantRabbitListener(AccountRepo accountRepo,
                                    DishRepo dishRepo,
                                    RabbitTemplate rabbitTemplate) {
        this.accountRepo = accountRepo;
        this.dishRepo = dishRepo;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "request_stock")
    public void handleStockRequests(String body) {
        try {
            JsonNode j = mapper.readTree(body);
            String userId = j.get("userId").asText();
            JsonNode dishes = j.get("dishes");
            boolean available = true;
            for (JsonNode dish : dishes) {
                Long dishId = dish.get("dishId").asLong();
                int amount = dish.get("amount").asInt();
                if (!dishRepo.isAvailable(dishId, amount)) {
                    available = false;
                    break;
                }
            }

            // create a response object
            com.fasterxml.jackson.databind.node.ObjectNode response = mapper.createObjectNode();
            response.put("userId", userId);
            response.put("available", available);
            rabbitTemplate.convertAndSend("response_stock", response.toString());
        } catch (Exception e) {
            // Optionally log the error
        }
    }

    @RabbitListener(queues = "order_success_queue")
    public void handleOrderSuccess(String message) {
        try {
            JsonNode json = mapper.readTree(message);
            JsonNode dishes = json.get("dishes");

            for (JsonNode dish : dishes) {
                Long dishId = dish.get("dishId").asLong();
                int amount = dish.get("amount").asInt();
                dishRepo.updateAmount(dishId, amount);
            }

        } catch (Exception e) {
            System.err.println("Error processing order success message: " + e.getMessage());
        }
    }

}
