package com.example.systemorder.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.systemorder.message.SendOrderConfirm;
import com.example.systemorder.models.Order;
import com.example.systemorder.models.RequestDishs;
import com.example.systemorder.repo.SystemOrderRepoLocal;
import com.example.systemorder.utilities.Calc;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

@Stateless
public class OrderService implements IOrderService {

    @EJB
    private SystemOrderRepoLocal systemOrderRepo;

    @EJB
    private SendOrderConfirm confirmPublisher;

    private static final double MIN_CHARGE = 10.0;

    private final Calc calc = new Calc();

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void placeOrder(Long userID, Long restaurantID, List<RequestDishs> requestDishes,
                           String destination, String shippingCompany, Double totalPrice) {
        try {
            // Prepare stock request
            Map<Long, Integer> qtyMap = mapDishQuantities(requestDishes);
            List<Map<String, Object>> dishes = prepareDishDetails(qtyMap);

            // Check stock availability
            boolean stockAvailable = checkStockAvailability(userID, dishes);
            if (!stockAvailable) {
                handleStockUnavailable(userID);
            }

            // Validate total price
            validateTotalPrice(userID, totalPrice);

            // Create and save the order
            Order order = createOrder(userID, restaurantID, qtyMap, totalPrice, destination, shippingCompany);
            systemOrderRepo.placeOrder(order);

            // Process payment
            boolean paymentSuccessful = processPayment(userID, totalPrice);
            if (!paymentSuccessful) {
                handlePaymentFailure(userID);
            }

            // Mark order as completed
            completeOrder(order, userID);

        } catch (Exception ex) {
            throw new RuntimeException("Order processing failed", ex);
        }
    }

    private Map<Long, Integer> mapDishQuantities(List<RequestDishs> requestDishes) {
        return requestDishes.stream()
                .collect(Collectors.toMap(RequestDishs::getDishID, RequestDishs::getQuantity));
    }

    private List<Map<String, Object>> prepareDishDetails(Map<Long, Integer> qtyMap) {
        return qtyMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> dish = new HashMap<>();
                    dish.put("dishId", entry.getKey());
                    dish.put("amount", entry.getValue());
                    return dish;
                }).collect(Collectors.toList());
    }

    private boolean checkStockAvailability(Long userID, List<Map<String, Object>> dishes) {
        Map<String, Object> stockRequest = new HashMap<>();
        stockRequest.put("userId", userID);
        stockRequest.put("dishes", dishes);

        String stockResponse = sendStockRequest(stockRequest, userID);
        return parseStockResponse(stockResponse);
    }

    private String sendStockRequest(Map<String, Object> stockRequest,Long userID) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            String correlationId = UUID.randomUUID().toString();

            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .correlationId(correlationId)
                    .build();

            ObjectMapper mapper = new ObjectMapper();
            String message = mapper.writeValueAsString(stockRequest);
            channel.basicPublish("stock_exchange", "request_stock", props, message.getBytes());

            return waitForStockResponse(channel, userID.toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to send stock request", e);
        }
    }

    private String waitForStockResponse(Channel channel, String userID) throws Exception {
        final String[] response = {null};
        channel.basicConsume("response_stock", true, (consumerTag, delivery) -> {
            String stockResponse = new String(delivery.getBody(), "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseNode = mapper.readTree(stockResponse);
            if (responseNode.get("userId").asText().equals(userID)) {
                response[0] = mapper.writeValueAsString(responseNode);
            }

        }, consumerTag -> {});

        while (response[0] == null) {
            Thread.sleep(100);
        }
        return response[0];
    }


    private boolean parseStockResponse(String stockResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseNode = mapper.readTree(stockResponse);
            return responseNode.get("available").asBoolean();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse stock response", e);
        }
    }

    private void handleStockUnavailable(Long userID) {
        confirmPublisher.send("failure", "Order failed: insufficient stock at restaurant", userID);
        confirmPublisher.log("OrderService", "Order failed: insufficient stock at restaurant for user=" + userID, "Error");
        throw new RuntimeException("Insufficient stock at restaurant");
    }

    private void validateTotalPrice(Long userID, Double totalPrice) {
        if (totalPrice < MIN_CHARGE) {
            confirmPublisher.send("failure", "Order failed: must be at least " + MIN_CHARGE, userID);
            confirmPublisher.log("OrderService", "Order below minimum charge: user=" + userID + " total=" + totalPrice, "Error");
            throw new RuntimeException("Below minimum charge");
        }
    }

    private Order createOrder(Long userID, Long restaurantID, Map<Long, Integer> qtyMap,
                              Double totalPrice, String destination, String shippingCompany) {
        List<Long> dishIds = new ArrayList<>(qtyMap.keySet());
        return new Order(userID, restaurantID, dishIds, BigDecimal.valueOf(totalPrice), destination, shippingCompany);
    }

    private boolean processPayment(Long userID, double totalPrice) {
        String paymentId = UUID.randomUUID().toString();
        try {
            boolean success = true; // Simulate payment processing
            if (success) {
                confirmPublisher.send("payments_exchange", "payment_success", "Payment processed", userID, paymentId, totalPrice, "EGP");
                confirmPublisher.log("OrderService", "Payment succeeded: user=" + userID + " paymentId=" + paymentId, "Info");
                return true;
            } else {
                confirmPublisher.send("payments_exchange", "payment_failure", "Payment failure", userID, paymentId, totalPrice, "EGP");
                confirmPublisher.log("OrderService", "Payment failed: user=" + userID + " paymentId=" + paymentId, "Error");
                return false;
            }
        } catch (Exception e) {
            confirmPublisher.send("payments_exchange", "payment_failure", "Payment error: " + e.getMessage(), userID, paymentId, totalPrice, "EGP");
            confirmPublisher.log("OrderService", "Payment exception: " + e.getMessage(), "Error");
            return false;
        }
    }

    private void handlePaymentFailure(Long userID) {
        confirmPublisher.send("failure", "Order failed: payment error", userID);
        confirmPublisher.log("OrderService", "Order failed: payment error for user=" + userID, "Error");
        throw new RuntimeException("PaymentFailed");
    }

    private void completeOrder(Order order, Long userID) {
        order.setStatus("Completed");
        systemOrderRepo.placeOrder(order);
        confirmPublisher.send("success", "Order placed successfully", userID);
        confirmPublisher.log("OrderService", "Order completed for user=" + userID + " orderId=" + order.getId(), "Info");
    }

    @Override
    public List<Order> getAllOrdersByUserID(Long userID) {
        return systemOrderRepo.getOrdersByUserId(userID);
    }

    @Override
    public List<Order> getAllOrdersByRestaurantID(Long restaurantID) {
        return systemOrderRepo.getOrdersByRestaurantId(restaurantID);
    }

    @Override
    public List<Order> getAllOrders() {
        return systemOrderRepo.getAllOrders();
    }
}
