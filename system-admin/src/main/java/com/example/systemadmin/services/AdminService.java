package com.example.systemadmin.services;

import com.example.systemadmin.utils.CompanyAccountResponse;
import com.example.systemadmin.utils.Cutomer;
import com.example.systemadmin.utils.Dish;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AdminService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<CompanyAccountResponse> createCompanyAccounts(List<String> names) {
        List<CompanyAccountResponse> responses = new ArrayList<>();

        for (String name : names) {
            //String password = generatePassword();
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("name", name);
            //requestBody.put("password", password);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<CompanyAccountResponse> response = restTemplate.postForEntity(
                        "http://localhost:8082/createAccount",
                        request,
                        CompanyAccountResponse.class
                );
                if (response.getStatusCode() == HttpStatus.CREATED) {
                    CompanyAccountResponse accountResponse = response.getBody();
                    if (accountResponse != null) {
                        responses.add(accountResponse);
                    }
                } else {
                    System.err.println("Failed to create account for: " + name);
                }
            } catch (Exception e) {
                System.err.println("Failed to create account for: " + name);
            }
        }

        return responses;
    }

    public List<Dish> getDishesByIds(List<Long> dishIds) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> request = new HttpEntity<>(dishIds, headers);

        try {
            ResponseEntity<Dish[]> response = restTemplate.postForEntity(
                    "http://localhost:8082/dishes/getbyids",
                    request,
                    Dish[].class
            );

            return response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public List<Cutomer> getAllUsers() {
        try {
            ResponseEntity<Cutomer[]> response = restTemplate.getForEntity(
                    "http://localhost:8083/accounts/all", // URL of user service
                    Cutomer[].class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Arrays.asList(response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 8); // 8-char random password
    }
}
