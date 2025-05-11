package com.example.restaurant.utilities;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Utility class to initialize database connection
 */
@Singleton
@Startup
public class DatabaseInitializer {
    
    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());
    
    @PostConstruct
    public void init() {
        try {
            LOGGER.info("Initializing database connection");
            
            String url = EnvLoader.get("DB_URL");
            String username = EnvLoader.get("DB_USERNAME");
            String password = EnvLoader.get("DB_PASSWORD");
            String driver = EnvLoader.get("DB_DRIVER");
            
            LOGGER.info("Database configuration:");
            LOGGER.info("URL: " + url);
            LOGGER.info("Username: " + username);
            LOGGER.info("Driver: " + driver);
            LOGGER.info("Password exists: " + (password != null && !password.isEmpty()));
            
            if (url == null || username == null || password == null || driver == null) {
                LOGGER.severe("Database configuration missing. Make sure .env file contains DB_URL, DB_USERNAME, DB_PASSWORD, and DB_DRIVER");
                return;
            }
            
            // Load the database driver
            Class.forName(driver);
            LOGGER.info("Driver loaded successfully: " + driver);
            
            // Test connection
            try {
                LOGGER.info("Attempting to connect to database at: " + url);
                Connection connection = DriverManager.getConnection(url, username, password);
                LOGGER.info("Database connection successful!");
                connection.close();
            } catch (SQLException e) {
                LOGGER.severe("Failed to connect to database: " + e.getMessage());
                // Additional error logging
                LOGGER.severe("SQLException State: " + e.getSQLState());
                LOGGER.severe("SQLException ErrorCode: " + e.getErrorCode());
            }
            
        } catch (ClassNotFoundException e) {
            LOGGER.severe("Database driver not found: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("An error occurred during database initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
