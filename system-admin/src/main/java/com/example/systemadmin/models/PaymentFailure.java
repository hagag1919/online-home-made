package com.example.systemadmin.models;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_failures")
@ToString
public class PaymentFailure implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "payment_id", nullable = false, length = 36)
    private String paymentId; // UUID as string

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId; // UUID as string

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @CreationTimestamp
    @Column(name = "occurred_at", updatable = false)
    private LocalDateTime occurredAt;

    @Column(nullable = false)
    private Boolean notified = false;
    // Default constructor
    public PaymentFailure() {
    }
    // Constructor with fields
    public PaymentFailure(String paymentId, String userId, double amount, String currency, String reason) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.amount = amount;
        this.currency = currency;
        this.reason = reason;
    }

    public Integer getId() {
        return id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public Boolean getNotified() {
        return notified;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }

    public void setNotified(Boolean notified) {
        this.notified = notified;
    }
}
