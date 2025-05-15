package com.example.systemorder.message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Startup
@Singleton
public class RabbitMQSetup {

    @PostConstruct
    public void init() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection conn = factory.newConnection();
             Channel ch = conn.createChannel()) {

            // Order direct exchange (success/failure)
            ch.exchangeDeclare("order_exchange", "direct", true);
            ch.queueDeclare("order_success_queue", true, false, false, null);
            ch.queueDeclare("order_failure_queue", true, false, false, null);
            ch.queueBind("order_success_queue", "order_exchange", "success");
            ch.queueBind("order_failure_queue", "order_exchange", "failure");

            // Payments direct exchange
            ch.exchangeDeclare("payments_exchange", "direct", true);
            ch.queueDeclare("payment_failure_queue", true, false, false, null);
            ch.queueBind("payment_failure_queue", "payments_exchange", "payment_failure");

            // Log topic exchange
            ch.exchangeDeclare("log_exchange", "topic", true);

            ch.queueDeclare("log_queue", true, false, false, null);
            ch.queueBind("log_queue", "log_exchange", "#.Error");   // Match all services with severity "Error"
            ch.queueBind("log_queue", "log_exchange", "#.Warning"); // Match all services with severity "Warning"
            ch.queueBind("log_queue", "log_exchange", "#.Info");    // Match all services with severity "Info"

            // Stock exchange
            ch.exchangeDeclare("stock_exchange", "topic", true);
            ch.queueDeclare("request_stock", true, false, false, null);
            ch.queueDeclare("response_stock", true, false, false, null);
            ch.queueBind("request_stock", "stock_exchange", "request_stock");
            ch.queueBind("response_stock", "stock_exchange", "response_stock");

        }
    }
}
