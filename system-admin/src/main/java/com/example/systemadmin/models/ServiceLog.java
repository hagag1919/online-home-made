package com.example.systemadmin.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_logs")
@ToString
public class ServiceLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "service_name", nullable = false, length = 100)
    private String serviceName;

    @Column(nullable = false, length = 10)
    private String severity;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "JSON")
    private String eventData;

    @CreationTimestamp
    @Column(name = "occurred_at", updatable = false)
    private LocalDateTime occurredAt;

    @Column(nullable = false)
    private Boolean notified = false;
    // Default constructor
    public ServiceLog() {
    }
    // Constructor with fields
    public ServiceLog(String serviceName, String severity, String message, String eventData) {
        this.serviceName = serviceName;
        this.severity = severity;
        this.message = message;
        this.eventData = eventData;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String getSeverity() {
        return severity;
    }
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getEventData() {
        return eventData;
    }
    public void setEventData(String eventData) {
        this.eventData = eventData;
    }
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
    public Boolean getNotified() {
        return notified;
    }
    public void setNotified(Boolean notified) {
        this.notified = notified;
    }

}
