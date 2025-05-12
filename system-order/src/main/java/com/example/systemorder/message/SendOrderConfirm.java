package com.example.systemorder.message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;

@Stateless
public class SendOrderConfirm {
    private static final String EXCHANGE = "order_exchange";

    public void send(String routingKey, String message, Long userId) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        String userQueue = "order_status_user_" + userId;
        try (Connection conn = factory.newConnection();
             Channel channel = conn.createChannel()) {
            channel.confirmSelect();
            channel.basicPublish(EXCHANGE, routingKey,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,

                    message.getBytes("UTF-8"));
               channel.queueDeclare(userQueue, true, false, false, null);
        channel.basicPublish("", userQueue,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes("UTF-8"));
            channel.waitForConfirmsOrDie(5000);
        } catch (Exception e) {
            throw new EJBException("Failed to send RabbitMQ message", e);
        }
    }
}