package com.example.systemadmin.controllers;

import com.example.systemadmin.models.PaymentFailure;
import com.example.systemadmin.models.ServiceLog;
import com.example.systemadmin.services.AdminService;
import com.example.systemadmin.utils.CompanyAccountRequest;
import com.example.systemadmin.utils.CompanyAccountResponse;
import com.example.systemadmin.utils.Cutomer;
import com.example.systemadmin.utils.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/payment-failures")
    public ResponseEntity<List<PaymentFailure>> getPaymentFailures() {
        return ResponseEntity.ok(adminService.getAllPaymentFailures());
    }

    @GetMapping("/service-logs")
    public ResponseEntity<List<ServiceLog>> getServiceLogs() {
        return ResponseEntity.ok(adminService.getAllServiceLogs());
    }

    @PostMapping("/create-company-accounts")
    public ResponseEntity<List<CompanyAccountResponse>> createAccounts(@RequestBody CompanyAccountRequest request) {
        List<CompanyAccountResponse> createdAccounts = adminService.createCompanyAccounts(request.getCompanyNames());
        return ResponseEntity.ok(createdAccounts);
    }

    @PostMapping("/list-dishes")
    public ResponseEntity<List<Dish>> getDishes(@RequestBody List<Long> dishIds) {
        List<Dish> dishes = adminService.getDishesByIds(dishIds);
        return ResponseEntity.ok(dishes);
    }
    @GetMapping("/list-users")
    public ResponseEntity<List<Cutomer>> listAllUsers() {
        List<Cutomer> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}

