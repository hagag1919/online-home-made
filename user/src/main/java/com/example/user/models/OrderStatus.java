package com.example.user.models;

import jakarta.persistence.*;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_status")
@ToString
@NamedQueries({
        @NamedQuery(name = "OrderStatus.findAll", query = "SELECT os FROM OrderStatus os"),
        @NamedQuery(name = "OrderStatus.findById", query = "SELECT os FROM OrderStatus os WHERE os.id = :id"),
        @NamedQuery(name = "OrderStatus.findByUserId", query = "SELECT os FROM OrderStatus os WHERE os.userId = :userId"),
        @NamedQuery(name = "OrderStatus.findByStatus", query = "SELECT os FROM OrderStatus os WHERE os.status = :status")
})
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "payment_id")
    private String paymentId;

    private Double amount;

    private String currency;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Constructor
    public OrderStatus() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
