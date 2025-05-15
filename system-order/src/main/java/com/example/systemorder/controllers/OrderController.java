package com.example.systemorder.controllers;

import com.example.systemorder.models.Order;
import com.example.systemorder.services.IOrderService;
import com.example.systemorder.utilities.OrderRequest;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {

    @EJB
    private IOrderService orderService;

    @GET
    @Path("/getAllOrdersByUserID")
    public Response getAllOrdersByUserID(@QueryParam("userID") Long userID) {
        return Response.ok(orderService.getAllOrdersByUserID(userID)).build();
    }

    @GET
    @Path("/getAllOrdersByRestaurantID")
    public Response getAllOrdersByRestaurantID(@QueryParam("restaurantID") Long restaurantID) {
        return Response.ok(orderService.getAllOrdersByRestaurantID(restaurantID)).build();
    }

    @POST
    @Path("/placeOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response placeOrder(OrderRequest orderRequest) {
        orderService.placeOrder(
                orderRequest.getUserID(),
                orderRequest.getRestaurantID(),
                orderRequest.getOrderDishes(),
                orderRequest.getDestination(),
                orderRequest.getShippingCompany(),
                orderRequest.getTotalPrice()
        );
        return Response.accepted().entity("Order received, being processed.").build();
    }

    @GET
    @Path("/getAllOrders")
    public Response getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return Response.ok(orders).build();
    }

    @GET
    @Path("/getOrderDishesByOrderID")
    public Response getOrderDishesByOrderID(@QueryParam("orderID") Long orderID) {
        List<Order> orders = orderService.getAllOrders();
        for (Order order : orders) {
            if (order.getId().equals(orderID)) {
                return Response.ok(order.getOrderDishes()).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Order not found").build();
    }

}
