package com.example.restaurant;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.example.restaurant.utilities.EnvLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main entry point for the Restaurant API.
 */
@Path("/restaurants")
public class OrderResource {

    /**
     * Root endpoint for the restaurant API.
     * 
     * @return A welcome message with API usage information.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiInfo() {
        Map<String, Object> apiInfo = Map.of(
            "name", "Restaurant Service API",
            "version", "1.0",
            "description", "API for restaurant seller operations",
            "endpoints", Map.of(
                "/api/auth/login", "Authenticate restaurant seller",
                "/api/restaurants/{restaurantId}/dishes", "Manage restaurant dishes",
                "/api/restaurants/{restaurantId}/orders", "View order history"
            )
        );
        
        return Response.ok(apiInfo).build();
    }

    // ping endpoint
    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public Response ping() {
        String mString = EnvLoader.get("DB_USERNAME");

        return Response.ok("Pong! " + mString).build();
    }
    /**
     * Endpoint to get the list of restaurants.
     * 
     * @return A response containing the list of restaurants.
     */

    /**
     * Test endpoint to execute a simple database query
     * 
     * @return A response containing the query results
     */
    @GET
    @Path("/dbtest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testDatabaseQuery() {
        List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Get database connection parameters
            String url = EnvLoader.get("DB_URL");
            String username = EnvLoader.get("DB_USERNAME");
            String password = EnvLoader.get("DB_PASSWORD");
            
            // Establish connection
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 Statement stmt = conn.createStatement()) {
                
                // Simple query to show database is working
                // This will work even if no tables exist yet
                String query = "SHOW TABLES";
                
                try (ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("tableName", rs.getString(1));
                        results.add(row);
                    }
                }
                
                // If no tables found, add information query
                if (results.isEmpty()) {
                    try (ResultSet rs = stmt.executeQuery("SELECT version() as version, database() as db")) {
                        if (rs.next()) {
                            results.add(Map.of(
                                "message", "No tables found in database. Server is working!",
                                "mysqlVersion", rs.getString("version"),
                                "currentDatabase", rs.getString("db")
                            ));
                        }
                    }
                }
                
                response.put("status", "success");
                response.put("message", "Database query executed successfully");
                response.put("results", results);
                
                return Response.ok(response).build();
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Database query failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                   .entity(response)
                   .build();
        }
    }
}