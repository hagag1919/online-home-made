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
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void placeOrder(Long userID,
            Long restaurantID,
            List<RequestDishs> requestDishes,
            String destination,
            String shippingCompany) {
        try {
            Map<Long, Integer> qtyMap = requestDishes.stream()
                    .collect(Collectors.toMap(RequestDishs::getDishID, RequestDishs::getQuantity));
            List<Dish> dishes = dishService.fetchDishesByIds(new ArrayList<>(qtyMap.keySet()));

            double total = 0;
            List<OrderDishes> ods = new ArrayList<>();
            for (Dish d : dishes) {
                int q = qtyMap.get(d.getId());
                if (q > d.getAmount()) {
                    confirmPublisher.send("failure",
                            "Order failed: insufficient stock for " + d.getName(),
                            userID);
                    confirmPublisher.log("OrderService",
                            "Order failed: insufficient stock for " + d.getName() + " user=" + userID,
                            "Error");
                    throw new RuntimeException("Insufficient stock");
                }
                total += calc.calcTotalPrice(d.getPrice(), q);
                OrderDishes od = new OrderDishes(d.getName(), q,
                        BigDecimal.valueOf(d.getPrice()),
                        d.getDescription());
                ods.add(od);
            }

            if (total < MIN_CHARGE) {
                confirmPublisher.send("failure",
                        "Order failed: must be at least " + MIN_CHARGE,
                        userID);
                confirmPublisher.log("OrderService",
                        "Order below minimum charge: user=" + userID + " total=" + total,
                        "Error");
                throw new RuntimeException("Below minimum charge");
            }

            Order order = new Order(userID, restaurantID, ods, BigDecimal.valueOf(total), destination, shippingCompany);
            order.setStatus("Pending");
            systemOrderRepo.placeOrder(order);

            boolean paid = processPayment(userID, total);
            if (!paid) {
                confirmPublisher.send("failure",
                        "Order failed: payment error",
                        userID);
                confirmPublisher.log("OrderService",
                        "Order failed: payment error for user=" + userID,
                        "Error");

                throw new RuntimeException("PaymentFailed");
            }
            confirmPublisher.log("OrderService",
                    "Order failed: payment error for user=" + userID,
                    "Error");

            order.setStatus("Completed");
            systemOrderRepo.placeOrder(order);

            confirmPublisher.send("success",
                    "Order placed successfully",
                    userID);
            confirmPublisher.log("OrderService",
                    "Order completed for user=" + userID + " orderId=" + order.getId(),
                    "Info");

        } catch (Exception ex) {
            // Any exception causes transaction rollback
            throw new RuntimeException("Order processing failed", ex);
        }
    }

    private boolean processPayment(Long userID, double totalPrice) {
        String paymentId = UUID.randomUUID().toString();
        try {
           
            boolean success =  false;
            if (success) {
                confirmPublisher.send(
                    "payments_exchange",           
                    "payment_success",            
                    "Payment processed",
                    userID, paymentId, totalPrice, "EGP"
                );
                confirmPublisher.log("OrderService",
                        "Payment succeeded: user=" + userID + " paymentId=" + paymentId,
                        "Info");
                return true;
            } else {
                confirmPublisher.send(
                    "payments_exchange",
                    "payment_failure",
                    "Payment failure",
                    userID, paymentId, totalPrice, "EGP"
                );
                confirmPublisher.log("OrderService",
                        "Payment failed: user=" + userID + " paymentId=" + paymentId,
                        "Error");
                return false;
            }
        } catch (Exception e) {
            confirmPublisher.send(
                "payments_exchange",
                "payment_failure",
                "Payment error: " + e.getMessage(),
                userID, paymentId, totalPrice, "EGP"
            );
            confirmPublisher.log("OrderService",
                    "Payment exception: " + e.getMessage(),
                    "Error");
            return false;
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
