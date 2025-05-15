package com.example.user.message;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitConsumerConfig {

    @Bean
    public Queue orderStatus() {
        return new Queue("order_status_user", true);
    }
}
