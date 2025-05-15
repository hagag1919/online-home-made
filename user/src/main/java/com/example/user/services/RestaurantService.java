package com.example.user.services;

import org.springframework.stereotype.Service;

import com.example.user.models.Dish;
import com.example.user.models.Restaurant;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class RestaurantService {

    public List<Restaurant> getAllRestaurants() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8082/getall"))
            .header("Accept", "*/*")
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());


        // convert the response body to a list of Restaurant objects

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // I want only to read id and name from the response
        List<Restaurant> restaurants = objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Restaurant.class));
        return restaurants;

        
    }
    
    public List<Dish> getDishesByRestaurantId(Long restaurantId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8082/dishes/getbyrestaurantid?restaurantId=" + restaurantId))
            .header("Accept", "*/*")
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Dish> dishes = objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Dish.class));
        return dishes;
    }
}