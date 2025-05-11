package com.example.systemorder.repo;

import com.example.systemorder.models.Order;
import com.example.systemorder.models.OrderDishes; // Added import
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class SystemOrderRepo implements SystemOrderRepoLocal {

    private static final Logger logger = Logger.getLogger(SystemOrderRepo.class.getName());

    @EJB
    private DBConnection dbConnection;

    @Override
    public void placeOrder(Order order) {

        String orderSql = "INSERT INTO Orders (user_id, restaurant_id, total_price, destination, shipping_company, status) VALUES (?, ?, ?, ?, ?, ?)";
        String orderDishesSql = "INSERT INTO OrderDishes (order_id, name, amount, price, description) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setLong(1, order.getUserID());
                orderStmt.setLong(2, order.getRestaurantID());
                orderStmt.setBigDecimal(3, order.getTotalPrice());
                orderStmt.setString(4, order.getDestination());
                orderStmt.setString(5, order.getShippingCompany());
                orderStmt.setString(6, order.getStatus());
                orderStmt.executeUpdate();

                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }

            if (order.getDishes() != null && !order.getDishes().isEmpty()) {
                try (PreparedStatement dishStmt = conn.prepareStatement(orderDishesSql)) {
                    for (OrderDishes dish : order.getDishes()) {
                        dishStmt.setLong(1, order.getId()); // Use the generated order ID
                        dishStmt.setString(2, dish.getName());
                        dishStmt.setInt(3, dish.getAmount());
                        dishStmt.setBigDecimal(4, dish.getPrice());
                        dishStmt.setString(5, dish.getDescription());
                        dishStmt.addBatch();
                    }
                    dishStmt.executeBatch();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            logger.severe("Error placing order: " + e.getMessage());
            if (conn != null) {
                try {
                    logger.info("Transaction is being rolled back");
                    conn.rollback();
                } catch (SQLException excep) {
                    logger.severe("Error rolling back transaction: " + excep.getMessage());
                }
            }
            throw new RuntimeException("Error placing order", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    logger.severe("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public List<Order> getOrdersByUserId(Long userID) {
        String sql = "SELECT * FROM Orders WHERE user_id = ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                // Optionally, load order dishes here if needed immediately
                // order.setDishes(getOrderDishesByOrderId(order.getId(), conn)); // Example
                orders.add(order);
            }

        } catch (SQLException e) {
            logger.severe("Error fetching orders by user ID: " + e.getMessage());
        }

        return orders;
    }

    @Override
    public List<Order> getOrdersByRestaurantId(Long restaurantID) {
        // Adjusted column name to snake_case
        String sql = "SELECT * FROM Orders WHERE restaurant_id = ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, restaurantID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                // Optionally, load order dishes here
                orders.add(order);
            }

        } catch (SQLException e) {
            logger.severe("Error fetching orders by restaurant ID: " + e.getMessage());
        }

        return orders;
    }

    @Override
    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM Orders";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                // Optionally, load order dishes here
                orders.add(order);
            }

        } catch (SQLException e) {
            logger.severe("Error fetching all orders: " + e.getMessage());
        }

        return orders;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserID(rs.getLong("user_id"));
        order.setRestaurantID(rs.getLong("restaurant_id"));
        order.setTotalPrice(rs.getBigDecimal("total_price"));
        order.setDestination(rs.getString("destination"));
        order.setShippingCompany(rs.getString("shipping_company"));
        order.setStatus(rs.getString("status"));
        return order;
    }


    private List<OrderDishes> getOrderDishesByOrderId(Long orderId, Connection conn) throws SQLException {
        String sql = "SELECT * FROM OrderDishes WHERE order_id = ?";
        List<OrderDishes> dishes = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderDishes dish = new OrderDishes();
                dish.setId(rs.getLong("id"));
                dish.setName(rs.getString("name"));
                dish.setAmount(rs.getInt("amount"));
                dish.setPrice(rs.getBigDecimal("price"));
                dish.setDescription(rs.getString("description"));
                dishes.add(dish);
            }
        }
        return dishes;
    }
}