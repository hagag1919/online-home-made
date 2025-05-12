package com.example.system_restaurant.controllers;

import com.example.system_restaurant.services.AccountService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.example.system_restaurant.models.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class AccountController {
    // This class will handle the HTTP requests related to accounts
    // It will use the AccountService to perform the operations

    @Autowired
    private AccountService accountService;

    // Method to create a new account
    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccount (@RequestBody Account account) {
        String name = account.getName();
        return accountService.createAccount(name);
    }

    // Method to login to an account
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Account account) {
        String name = account.getName();
        String password = account.getPassword();
        return accountService.login(name, password);
    }

    
    

}