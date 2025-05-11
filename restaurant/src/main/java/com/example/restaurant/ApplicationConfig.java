package com.example.restaurant;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configures JAX-RS for the application.
 */
@ApplicationPath("/api")
public class ApplicationConfig extends Application {
}