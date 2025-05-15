package com.example.systemorder.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class CorsFilter implements ContainerResponseFilter {
    private static final Logger logger = Logger.getLogger(CorsFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        // Check if CORS headers are already present (to avoid duplicates)
        if (!responseContext.getHeaders().containsKey("Access-Control-Allow-Origin")) {
            // Apply CORS headers only if they're not already set
            logger.info("Adding CORS headers to response");
            responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
            responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
            responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders().putSingle("Access-Control-Max-Age", "1209600");
        } else {
            logger.info("CORS headers already present, skipping...");
        }
    }
}
