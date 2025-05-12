package com.example.systemorder.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.systemorder.message.SendOrderConfirm;
import com.example.systemorder.models.Dish;
import com.example.systemorder.models.Order;
import com.example.systemorder.models.OrderDishes;
import com.example.systemorder.repo.SystemOrderRepoLocal;
import com.example.systemorder.utilities.Calc;

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

    @Override
    public void placeOrder(Long userID, Long restaurantID, List<Dish> dishes, String destination, String shippingCompany) {

        //! Must do : make List<Dish> dishes to be List<Long> dishesID and gets the dishes from exposed api from restaurant service
        //! Must do : check the amount of each dish and the total price of the order
        List<OrderDishes> orderDishes = new ArrayList<>();
        Double totalPrice = 0.0;

        for (Dish dish : dishes) {
            OrderDishes orderDish = new OrderDishes();
            orderDish.setName(dish.getName());
            orderDish.setPrice(BigDecimal.valueOf(dish.getPrice()));
            orderDish.setAmount(dish.getAmount());
            orderDish.setDescription(dish.getDescription());
            orderDishes.add(orderDish);
            totalPrice += calc.calcTotalPrice(dish.getPrice(), dish.getAmount()); //! this will change to the price of the dish from the restaurant service
        }
        Order order = new Order();
        order.setUserID(userID);
        order.setRestaurantID(restaurantID);
        order.setDestination(destination);
        order.setShippingCompany(shippingCompany);
        order.setDishes(orderDishes);
        order.setTotalPrice(BigDecimal.valueOf(totalPrice));
        order.setStatus("Pending");

        // 1. Validate stock
        // if (!inventory.hasStock(order)) {
        //     confirmPublisher.send("failure",
        //             "Order " + order.getId() + " failed: insufficient stock");
        // Throw runtime exception to rollback DB (container-managed TX)
        //     throw new RuntimeException("Insufficient stock");
        // }
        if (!calc.isAccepted(totalPrice, MIN_CHARGE)) {
            confirmPublisher.send("failure",
                    "Order " + order.getId() + " failed: total below minimum", userID);
            throw new RuntimeException("Below minimum charge");

        }

        systemOrderRepo.placeOrder(order);

        confirmPublisher.send("success",
                "Order " + order.getId() + " placed successfully", userID);

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
