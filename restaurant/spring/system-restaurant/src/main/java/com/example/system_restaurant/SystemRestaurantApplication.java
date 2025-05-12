package com.example.system_restaurant;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.system_restaurant.models.*;
import com.example.system_restaurant.services.*;

@SpringBootApplication
@RestController
public class SystemRestaurantApplication {

	private final AccountService accountService;

	public SystemRestaurantApplication(AccountService accountService) {
		this.accountService = accountService;
	}

	public static void main(String[] args) {
		SpringApplication.run(SystemRestaurantApplication.class, args);
	}
	@GetMapping
	public String hello() {
		return "Hello, World!";
	}

	@GetMapping("/getall")
	public List<Account> getAllCompanies(){
		return accountService.findAll();

	}
}
