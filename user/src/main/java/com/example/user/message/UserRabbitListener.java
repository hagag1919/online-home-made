package com.example.user.message;

import com.example.user.models.OrderStatus;
import com.example.user.repo.OrderStatusRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;



@Component
public class UserRabbitListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OrderStatusRepository repository;

    public UserRabbitListener(OrderStatusRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "order_status_user")
    public void receiveOrderStatus(String message) {
        try {
            Map<String, Object> map = objectMapper.readValue(message, Map.class);

            OrderStatus status = new OrderStatus();
            status.setUserId(((Number) map.get("userId")).longValue());
            status.setPaymentId((String) map.get("paymentId"));
            status.setAmount(map.get("amount") != null ? Double.valueOf(map.get("amount").toString()) : null);
            status.setCurrency((String) map.get("currency"));
            status.setStatus((String) map.get("type"));
            status.setReason((String) map.get("reason"));

            repository.save(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
