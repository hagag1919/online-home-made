package com.example.user.services;
import com.example.user.models.Account;
import com.example.user.repo.AccountRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    
    @Autowired
    private final AccountRepository accountRepository;
    
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    /**
     * Register a new user account
     * 
     * @param username the username of the new account
     * @param password the password of the new account
     * @return the created Account object
     */
    public Account register(String username, String password,Double balance) {
        if (accountRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        Account account = new Account(username, password,balance);
        return accountRepository.save(account);
    }
    /**
     * Find an account by username
     * 
     * @param username the username to search for
     * @return the Account object if found, null otherwise
     */
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username).orElse(null);
    }
    /**
     * Check if an account with the given username exists
     * 
     * @param username the username to check
     * @return true if an account with the username exists, false otherwise
     */
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    /**
     * Login a user account
     *  
     * @param username the username of the account
     * @param password the password of the account
     * @return the Account object if login is successful, null otherwise
     */
    public Account login(String username, String password) {
        Account account = findByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Double getUserBalance(Long id) {
        return accountRepository.getUserBalance(id);
    }
    public void updateUserBalance(Long id, Double balance) {
        accountRepository.updateUserBalance(id, balance);
    }
}