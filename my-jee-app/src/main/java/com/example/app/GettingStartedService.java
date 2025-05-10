package com.example.app;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GettingStartedService {

    public String hello(String name) {
        return String.format("Hello, " +  name);
    }
}