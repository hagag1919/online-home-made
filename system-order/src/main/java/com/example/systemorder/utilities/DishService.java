package com.example.systemorder.utilities;

import com.example.systemorder.models.Dish;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;
import jakarta.json.JsonObject;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class DishService {

    public List<Dish> fetchDishesByIds(List<Long> dishIds) {
        String url = "http://localhost:8082/dishes/getbyids";


        Client client = ClientBuilder.newClient();

        try {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (Long id : dishIds) {
                arrayBuilder.add(id);
            }
            JsonArray jsonArray = arrayBuilder.build();

            WebTarget target = client.target(url);

            Response response = target.request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(jsonArray.toString(), MediaType.APPLICATION_JSON));

            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                throw new RuntimeException("Failed to fetch dishes: HTTP error code " + response.getStatus());
            }

            String jsonResponse = response.readEntity(String.class);
            JsonReader reader = Json.createReader(new StringReader(jsonResponse));
            JsonArray dishesArray = reader.readArray();

            List<Dish> dishes = new ArrayList<>();
            for (int i = 0; i < dishesArray.size(); i++) {
                JsonObject dishJson = dishesArray.getJsonObject(i);
                Dish dish = new Dish();
                dish.setId(dishJson.getJsonNumber("id").longValue());
                dish.setName(dishJson.getString("name"));
                dish.setPrice(dishJson.getJsonNumber("price").doubleValue());
                dish.setDescription(dishJson.getString("description"));
                dish.setAmount(dishJson.getInt("amount"));
                dishes.add(dish);
            }

            return dishes;
        } finally {
            client.close();
        }
    }
}
