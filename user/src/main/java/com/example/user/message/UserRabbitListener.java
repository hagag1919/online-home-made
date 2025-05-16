package com.example.user.message;

import com.example.user.models.OrderStatus;
import com.example.user.repo.AccountRepository;
import com.example.user.repo.OrderStatusRepository;
import com.example.user.services.AccountService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;



@Component
public class UserRabbitListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OrderStatusRepository repository;
    private final AccountRepository accountRepository;

    public UserRabbitListener(OrderStatusRepository repository, AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    @RabbitListener(queues = "order_status_user")
    public void receiveOrderStatus(String message) {
        try {
            Map map = objectMapper.readValue(message, Map.class);

            OrderStatus status = new OrderStatus();
            status.setUserId(((Number) map.get("userId")).longValue());
            status.setPaymentId((String) map.get("paymentId"));
            status.setAmount(map.get("amount") != null ? Double.valueOf(map.get("amount").toString()) : null);
            status.setCurrency((String) map.get("currency"));
            status.setStatus((String) map.get("type"));
            status.setReason((String) map.get("reason"));

            repository.save(status);

            Double amount = status.getAmount();
            Long userId = status.getUserId();
            if (amount != null && amount > 0 && userId != null) {
                Double balance = accountRepository.getUserBalance(userId);
                balance -= amount;
                if(balance < 0) {
                    balance = 0.0;
                }
                accountRepository.updateUserBalance(userId, balance);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
