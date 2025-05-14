package com.example.systemadmin.confg;

import com.example.systemadmin.models.PaymentFailure;
import com.example.systemadmin.models.ServiceLog;
import com.example.systemadmin.repos.IPaymentFailureRepo;
import com.example.systemadmin.repos.IServiceLogRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.*;
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
