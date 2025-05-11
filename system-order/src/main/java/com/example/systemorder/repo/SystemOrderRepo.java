// filepath: d:\Level four\second semester\Distributed Systems\Microservices\system-order\src\main\java\com\example\systemorder\repo\SystemOrderRepo.java
package com.example.systemorder.repo;

import com.example.systemorder.models.Order;
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
        String sql = "INSERT INTO Orders (userID, restaurantID, totalPrice, destination, shippingCompany, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, order.getUserID());
            stmt.setLong(2, order.getRestaurantID());
            stmt.setBigDecimal(3, order.getTotalPrice());
            stmt.setString(4, order.getDestination());
            stmt.setString(5, order.getShippingCompany());
            stmt.setString(6, order.getStatus());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getLong(1));
                }
            }

        } catch (SQLException e) {
            logger.severe("Error placing order: " + e.getMessage());
            throw new RuntimeException("Error placing order", e);
        }
    }

    @Override
    public List<Order> getOrdersByUserId(Long userID) {
        String sql = "SELECT * FROM Orders WHERE userID = ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }

        } catch (SQLException e) {
            logger.severe("Error fetching orders by user ID: " + e.getMessage());
        }

        return orders;
    }

    @Override
    public List<Order> getOrdersByRestaurantId(Long restaurantID) {
        String sql = "SELECT * FROM Orders WHERE restaurantID = ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, restaurantID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
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
                orders.add(mapResultSetToOrder(rs));
            }

        } catch (SQLException e) {
            logger.severe("Error fetching all orders: " + e.getMessage());
        }

        return orders;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserID(rs.getLong("userID"));
        order.setRestaurantID(rs.getLong("restaurantID"));
        order.setTotalPrice(rs.getBigDecimal("totalPrice"));
        order.setDestination(rs.getString("destination"));
        order.setShippingCompany(rs.getString("shippingCompany"));
        order.setStatus(rs.getString("status"));
        return order;
    }
}