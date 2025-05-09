package com.example.systemorder;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

/**
 * A simple REST API for orders.
 */
@Path("/orders")
public class OrderResource {

    /**
     * A simple GET endpoint to fetch a welcome message.
     * 
     * @return A welcome message.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getOrders() {
        return "Welcome to the Orders API!";
    }
}