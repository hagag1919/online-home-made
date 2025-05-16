package com.example.systemorder.services;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

public class AccountService {

    String URL = "http://localhost:8083/accounts/getUserBalance";
    Client client = ClientBuilder.newClient();

    public double getUserBalance(long userID) {
        String url = URL + "?id=" + userID;
        try {
            String response = client.target(url)
                    .request()
                    .get(String.class);
            return Double.parseDouble(response);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
