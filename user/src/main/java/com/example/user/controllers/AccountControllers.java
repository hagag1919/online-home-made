package com.example.user.controllers;
import com.example.user.models.Account;
import com.example.user.models.OrderStatus;
import com.example.user.repo.OrderStatusRepository;
import com.example.user.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "*")
public class AccountControllers {
    
    @Autowired
    private final AccountService accountService;

    @Autowired
    private final OrderStatusRepository orderStatusRepository;
    
    public AccountControllers(AccountService accountService, OrderStatusRepository orderStatusRepository) {
        this.accountService = accountService;
        this.orderStatusRepository = orderStatusRepository;
    }
    
    /**
     * Register a new user account
     * 
     * @param account the account object containing username and password
     * @return ResponseEntity with the created Account object and HTTP status
     */
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Account createdAccount = accountService.register(account.getUsername(), account.getPassword(),account.getBalance());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdAccount.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdAccount);
    }
    /**
     * Login a user account
     * 
     * @param username the username of the account
     * @param password the password of the account
     * @return ResponseEntity with the Account object if login is successful, or an error message
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Username and password are required");
        }
        account = accountService.login(username, password);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    /**
     * Get all accounts
     * 
     * @return ResponseEntity with a list of all Account objects
     */
    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
    /**
     * Get an account by ID
     * 
     * @param id the ID of the account
     * @return ResponseEntity with the Account object if found, or an error message
     */
    @GetMapping("/getbyid")
    public ResponseEntity<?> getAccountById(@RequestParam Long id) {
        Account account = accountService.getAccountById(id);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
    }
    @GetMapping("/getOrderStatus")
    public  ResponseEntity<?> getOrderStatus(@RequestParam Long userId) {
        List<OrderStatus> orderStatuses = orderStatusRepository.findByUserId(userId);
        if (orderStatuses != null && !orderStatuses.isEmpty()) {
            return ResponseEntity.ok(orderStatuses);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No order status found for this user");
        }

    }

    @GetMapping("/getUserBalance")
    public ResponseEntity<?> getUserBalance(@RequestParam Long id) {
        Double balance = accountService.getUserBalance(id);
        if (balance != null) {
            return ResponseEntity.ok(balance);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
    @PutMapping("/updateUserBalance")
    public ResponseEntity<?> updateUserBalance(@RequestParam Long id, @RequestParam Double balance) {
        accountService.updateUserBalance(id, balance);
        return ResponseEntity.ok("User balance updated successfully");
    }
}