package com.example.user.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.ToString;

import java.util.Date;

import com.fasterxml.jackson.databind.util.Named;

/**
 * Account entity representing the user account in the system.
 * This model supports the basic functionalities for a customer:
 * - Registration
 * - Login
 * - User identification for orders
 */
@Entity
@Table(name = "users")
@ToString
@NamedQueries({
    // get all users
    // get user by username

    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findByUsername", query = "SELECT a FROM Account a WHERE a.username = :username"),
    @NamedQuery(name = "Account.findById", query = "SELECT a FROM Account a WHERE a.id = :id"),
        @NamedQuery(name = "Account.getUserBalance",query = "SELECT a.balance FROM Account a WHERE a.id = :id"),
        @NamedQuery(name = "Account.updateUserBalance",query = "UPDATE Account a SET a.balance = :balance WHERE a.id = :id"),
})
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name  = "username", nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(name = "password",nullable = false)
    private String password;


    private Double balance;

    
    // Default constructor required by JPA
    public Account() {
    }
    
    // Constructor with fields
    public Account(String username, String password, Double balance) {
        this.username = username;
        this.password = password;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
