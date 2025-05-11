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
    public void setupQueuesAndExchange() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection conn = factory.newConnection();
             Channel channel = conn.createChannel()) {

            channel.exchangeDeclare("order_exchange", "direct", true);

            channel.queueDeclare("order_success_queue", true, false, false, null);
            channel.queueDeclare("order_failure_queue", true, false, false, null);

            channel.queueBind("order_success_queue", "order_exchange", "success");
            channel.queueBind("order_failure_queue", "order_exchange", "failure");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
