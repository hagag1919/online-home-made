package com.example.systemorder.repo;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Singleton
@Startup
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ordersdb";
    private static final String USER = "bakry";
    private static final String PASSWORD = "TopG123";

    @PostConstruct
    public void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // This is where your error message originates
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
}