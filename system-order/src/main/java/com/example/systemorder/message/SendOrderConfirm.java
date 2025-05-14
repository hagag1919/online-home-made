package com.example.systemorder.message;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import jakarta.ejb.Stateless;

@Stateless
public class SendOrderConfirm {
    private static final ObjectMapper M = new ObjectMapper();
    private ConnectionFactory factory;
    {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
    }

    public void send(String routingKey, String msg, Long userId) {
        send("order_exchange", routingKey, msg, userId, null, null, null);
    }

    public void send(String type, String routingKey, String msg, Long userId,
                     String paymentId, Double amount, String currency) {
        try (Connection c = factory.newConnection();
             Channel ch = c.createChannel()) {

            ch.confirmSelect();
            Map<String,Object> p = new HashMap<>();
            p.put("type", routingKey);
            p.put("userId", userId);
            p.put("paymentId", paymentId);
            p.put("amount", amount);
            p.put("currency", currency);
            p.put("reason", msg);
            String json = M.writeValueAsString(p);

            // Direct or payments exchange
            String exch = type.equals("payments_exchange")
                          ? "payments_exchange" : "order_exchange";
            ch.basicPublish(exch, routingKey,
                MessageProperties.PERSISTENT_TEXT_PLAIN, json.getBytes());

            // customer queue
            String userQ = "order_status_user_" + userId;
            ch.queueDeclare(userQ,true,false,false,null);
            ch.basicPublish("", userQ,
                MessageProperties.PERSISTENT_TEXT_PLAIN, json.getBytes());

            ch.waitForConfirmsOrDie(5_000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void log(String service, String message, String severity) {
        try (Connection c = factory.newConnection();
             Channel ch = c.createChannel()) {

            ch.confirmSelect();
            Map<String, Object> logMessage = new HashMap<>();
            logMessage.put("service", service);
            logMessage.put("message", message);
            logMessage.put("severity", severity);
            String json = M.writeValueAsString(logMessage);

            // Sanitize service and severity to ensure valid routing key
            String sanitizedService = service.replaceAll("[^\\w]", "_");
            String sanitizedSeverity = severity.replaceAll("[^\\w]", "_");

            // Construct routing key
            String routingKey = sanitizedService + "." + sanitizedSeverity;

            ch.basicPublish("log_exchange", routingKey,
                    MessageProperties.PERSISTENT_TEXT_PLAIN, json.getBytes());

            ch.waitForConfirmsOrDie(5_000);
        } catch (Exception e) {
            throw new RuntimeException("Failed to log message", e);
        }
    }

}
