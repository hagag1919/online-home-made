package com.example.systemorder.message;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;

@Stateless
public class SendOrderConfirm {
    private static final String EXCHANGE = "order_exchange";
    private static final String LOG_EXCHANGE = "log_exchange";
    private static final ObjectMapper mapper = new ObjectMapper();

    public void send(String routingKey, String message, Long userId) {
        send(routingKey, message, userId, null, null, null);
    }

    public void send(String routingKey, String message, Long userId,
                     String paymentId, Double amount, String currency) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        String userQueue = "order_status_user_" + userId;

        try (Connection conn = factory.newConnection();
             Channel channel = conn.createChannel()) {

            channel.confirmSelect();

            Map<String, Object> payload = new HashMap<>();
            payload.put("type", routingKey);
            payload.put("userId", userId);
            payload.put("paymentId", paymentId);
            payload.put("amount", amount);
            payload.put("currency", currency);
            payload.put("reason", message);

            String jsonMessage = mapper.writeValueAsString(payload);

            // Send to direct exchange for admin listener
            channel.basicPublish(EXCHANGE, routingKey,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    jsonMessage.getBytes("UTF-8"));

            // Send to user-specific queue for UI or notifications
            channel.queueDeclare(userQueue, true, false, false, null);
            channel.basicPublish("", userQueue,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    jsonMessage.getBytes("UTF-8"));

            channel.waitForConfirmsOrDie(5000);
        } catch (Exception e) {
            throw new EJBException("Failed to send RabbitMQ message", e);
        }
    }

    public void log(String serviceName, String message, String severity) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection conn = factory.newConnection();
             Channel channel = conn.createChannel()) {

            channel.exchangeDeclare(LOG_EXCHANGE, "topic", true);

            Map<String, Object> logPayload = new HashMap<>();
            logPayload.put("serviceName", serviceName);
            logPayload.put("severity", severity);
            logPayload.put("message", message);

            String jsonLog = mapper.writeValueAsString(logPayload);

            String routingKey = serviceName + "_" + severity;
            channel.basicPublish(LOG_EXCHANGE, routingKey,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    jsonLog.getBytes("UTF-8"));

        } catch (Exception e) {
            throw new EJBException("Failed to send log message", e);
        }
    }
}