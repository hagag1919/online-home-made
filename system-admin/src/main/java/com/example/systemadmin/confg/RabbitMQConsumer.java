package com.example.systemadmin.confg;


import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.systemadmin.models.PaymentFailure;
import com.example.systemadmin.models.ServiceLog;
import com.example.systemadmin.repos.IPaymentFailureRepo;
import com.example.systemadmin.repos.IServiceLogRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.Envelope;

@Component
public class RabbitMQConsumer implements InitializingBean {

    @Autowired
    private IPaymentFailureRepo paymentFailureRepo;

    @Autowired
    private IServiceLogRepo serviceLogRepo;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterPropertiesSet() {
        listenToQueues();
    }

    public void listenToQueues() {
        new Thread(() -> {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();

                channel.queueDeclare("order_failure_queue", true, false, false, null);
                channel.queueDeclare("admin_error_logs", true, false, false, null);
                channel.queueDeclare("payment_failure", true, false, false, null);

                channel.basicConsume("order_failure_queue", true, new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        getFailureConsumer(channel).handle(consumerTag, new Delivery(envelope, properties, body));
                    }
                });
                channel.basicConsume("payment_failure", true, (consumerTag, message) -> getPaymentFailureConsumer(channel).handle(consumerTag, message), consumerTag -> {});
                channel.basicConsume("admin_error_logs", getLogConsumer(channel), consumerTag -> {});

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    private DeliverCallback getFailureConsumer(Channel channel) {
        return (consumerTag, message) -> {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("[AdminConsumer] Received failure: " + json);
        };
    }

    private DeliverCallback getPaymentFailureConsumer(Channel channel) {
        return (consumerTag, message) -> {
            try {
                String json = new String(message.getBody(), StandardCharsets.UTF_8);
                JsonNode node = objectMapper.readTree(json);

                PaymentFailure failure = new PaymentFailure();
                failure.setPaymentId(node.get("paymentId").asText());
                failure.setUserId(String.valueOf(node.get("userId").asLong()));
                failure.setAmount(node.get("amount").asDouble());
                failure.setCurrency(node.get("currency").asText());
                failure.setReason(node.get("reason").asText());

                paymentFailureRepo.save(failure);
                System.out.println("[AdminConsumer] Payment failure saved.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private DeliverCallback getLogConsumer(Channel channel) {
        return (consumerTag, message) -> {
            try {
                String json = new String(message.getBody(), StandardCharsets.UTF_8);
                JsonNode node = objectMapper.readTree(json);

                ServiceLog log = new ServiceLog();
                log.setServiceName(node.get("serviceName").asText());
                log.setSeverity(node.get("severity").asText());
                log.setMessage(node.get("message").asText());
                log.setEventData(json);

                serviceLogRepo.save(log);
                System.out.println("[AdminConsumer] Log saved.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
