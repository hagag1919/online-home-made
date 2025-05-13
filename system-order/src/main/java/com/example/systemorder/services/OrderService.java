package com.example.systemorder.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.systemorder.message.SendOrderConfirm;
import com.example.systemorder.models.Dish;
import com.example.systemorder.models.Order;
import com.example.systemorder.models.OrderDishes;
import com.example.systemorder.models.RequestDishs;
import com.example.systemorder.repo.SystemOrderRepoLocal;
import com.example.systemorder.utilities.Calc;
import com.example.systemorder.utilities.DishService;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class OrderService implements IOrderService {

    @EJB
    private SystemOrderRepoLocal systemOrderRepo;

    @EJB
    private SendOrderConfirm confirmPublisher;

    private static final double MIN_CHARGE = 10.0;

    private Calc calc = new Calc();

    private DishService dishService = new DishService();

    @Override
    public void placeOrder(Long userID, Long restaurantID, List<RequestDishs> requestDishes, String destination, String shippingCompany) {
        try {
            // Extract dish IDs
            List<Long> dishIds = requestDishes.stream()
                    .map(RequestDishs::getDishID)
                    .collect(Collectors.toList());

            List<Dish> fetchedDishes = dishService.fetchDishesByIds(dishIds);

            Map<Long, Integer> dishIdToQuantity = requestDishes.stream()
                    .collect(Collectors.toMap(RequestDishs::getDishID, RequestDishs::getQuantity));

            List<OrderDishes> orderDishes = new ArrayList<>();
            double totalPrice = 0.0;

            for (Dish dish : fetchedDishes) {
                Long dishId = dish.getId();
                Integer quantity = dishIdToQuantity.get(dishId);
                if (quantity > dish.getAmount()) {
                    confirmPublisher.send("failure",
                            "Order failed: quantity of dishes not available on stock", userID);
                    throw new RuntimeException("Dish " + dish.getName() + " is not available in the requested quantity.");
                }
                OrderDishes orderDish = new OrderDishes();
                orderDish.setName(dish.getName());
                orderDish.setAmount(quantity);
                orderDish.setPrice(BigDecimal.valueOf(dish.getPrice()));
                orderDish.setDescription(dish.getDescription());
                orderDishes.add(orderDish);
                totalPrice += calc.calcTotalPrice(dish.getPrice(), quantity);
            }

            if (!calc.isAccepted(totalPrice, MIN_CHARGE)) {
                confirmPublisher.send("failure",
                        "Order failed: total below minimum", userID);
                confirmPublisher.log("OrderService_Error", "Order below minimum charge for user " + userID, "Error");
                throw new RuntimeException("Below minimum charge");
            }

            Order order = new Order();
            order.setUserID(userID);
            order.setRestaurantID(restaurantID);
            order.setDestination(destination);
            order.setShippingCompany(shippingCompany);
            order.setDishes(orderDishes);
            order.setTotalPrice(BigDecimal.valueOf(totalPrice));
            order.setStatus("Pending");

            systemOrderRepo.placeOrder(order);

            // Simulate payment processing
            boolean paymentSuccess = processPayment(userID, totalPrice);
            if (!paymentSuccess) {
                confirmPublisher.send("failure",
                        "Order failed: payment processing failed", userID);
                confirmPublisher.log("OrderService_Error", "Payment failed for user " + userID, "Error");
                throw new RuntimeException("Payment processing failed");
            }

            order.setStatus("Completed");
            systemOrderRepo.placeOrder(order);

            confirmPublisher.send("success",
                    "Order placed successfully", userID);
            confirmPublisher.log("OrderService_Info", "Order placed successfully for user " + userID, "Info");

        } catch (Exception e) {
            confirmPublisher.send("failure", "Order failed: " + e.getMessage(), userID);
            confirmPublisher.log("OrderService_Error", "Order processing failed: " + e.getMessage(), "Error");
            throw new RuntimeException("Order processing failed", e);
        }
    }

   private boolean processPayment(Long userID, double totalPrice) {
    // Simulate payment logic
    String paymentId = UUID.randomUUID().toString();
    Double amount = totalPrice;
    String currency = "EGP";

    try {
        boolean paymentSuccess = true;

        if (paymentSuccess) {
            
            confirmPublisher.send("payment_success", "Payment processed successfully", userID, paymentId, amount, currency);
            confirmPublisher.log("OrderService_Info", "Payment processed successfully for user " + userID, "Info");
            return true;
        } else {
            // Notify RabbitMQ of failed payment
            confirmPublisher.send("payment_failure", "Payment failed", userID, paymentId, amount, currency);
            confirmPublisher.log("OrderService_Error", "Payment failed for user " + userID, "Error");
            return false;
        }
    } catch (Exception e) {
        // Log and rethrow the exception
        confirmPublisher.log("OrderService_Error", "Payment processing error: " + e.getMessage(), "Error");
        throw new RuntimeException("Payment processing error", e);
    }
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