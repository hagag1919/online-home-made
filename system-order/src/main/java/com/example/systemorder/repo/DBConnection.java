package com.example.systemorder.repo;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import javax.sql.DataSource;
import jakarta.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;

@Singleton
@Startup
public class DBConnection {

    @Resource(lookup = "java:/jdbc/ordersdb")  // JNDI name you configure in your app server
    private DataSource ds;

    @PostConstruct
    private void init() {
        // nothing required here if DataSource is configured in container
    }

    public Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
}
