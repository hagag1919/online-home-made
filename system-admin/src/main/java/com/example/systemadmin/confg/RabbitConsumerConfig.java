package com.example.systemadmin.confg;



import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitConsumerConfig {

    // Exchanges & queues are assumed already declared by Order service

    @Bean
    public Queue paymentFailureQueue() {
        return new Queue("payment_failure_queue", true);
    }

    @Bean
    public Queue adminErrorLogsQueue() {
        return new Queue("log_queue", true);
    }
}
