package com.example.system_restaurant.services;


import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.system_restaurant.models.Account;
import com.example.system_restaurant.repo.AccountRepo;
import com.example.system_restaurant.utils.AccountUtils;


@Service
public class AccountService {

    private final AccountRepo accountRepo;

    public AccountService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    public Account findById(Long id) {
        return accountRepo.findById(id).orElse(null);
    }

    public Account findByName(String name) {
        return accountRepo.findByName(name);
    }

    public void save(Account account) {
        accountRepo.save(account);
    }

    public void delete(Long id) {
        accountRepo.deleteById(id);
    }

    public List<Account> findAll() {
        return accountRepo.findAll();
    }

    public ResponseEntity<Account> createAccount(String name){

        if (accountRepo.findByName(name) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            Account account = new Account();
            account.setName(name);

            String password = AccountUtils.generateRandomPassword(8);
            account.setPassword(password);
            accountRepo.save(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(account);

        }
    }

    public ResponseEntity<?> login (String name, String password) {
        Account account = accountRepo.findByName(name);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        } else if (!account.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        } else {
            return ResponseEntity.ok(account);
        }
    }
    

}