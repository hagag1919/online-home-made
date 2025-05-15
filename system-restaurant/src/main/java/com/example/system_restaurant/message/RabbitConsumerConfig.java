package com.example.system_restaurant.message;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitConsumerConfig {

    @Bean
    public Queue requestStockAvailability() {
        return new Queue("request_stock", true);
    }

    @Bean
    public Queue replayStockAvailability() {
        return new Queue("response_stock", true);
    }

    @Bean
    public Queue orderStatus() {
        return new Queue("order_success_queue", true);
    }
}
