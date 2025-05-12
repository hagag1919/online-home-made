package com.example.system_restaurant.repo;
import com.example.system_restaurant.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Long> {

    Account findByName(String name);
}