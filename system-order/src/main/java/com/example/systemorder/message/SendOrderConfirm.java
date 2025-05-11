package com.example.systemorder.message;

import jakarta.ejb.Stateless;
import jakarta.ejb.EJBException;
import com.rabbitmq.client.*;

@Stateless
public class SendOrderConfirm {
    private static final String EXCHANGE = "order_exchange";

    public void send(String routingKey, String message) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection conn = factory.newConnection();
             Channel channel = conn.createChannel()) {
            channel.confirmSelect();
            channel.basicPublish(EXCHANGE, routingKey,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("UTF-8"));
            channel.waitForConfirmsOrDie(5000);
        } catch (Exception e) {
            throw new EJBException("Failed to send RabbitMQ message", e);
        }
    }
}
