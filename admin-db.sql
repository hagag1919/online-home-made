create database admin_db;
use admin_db;

-- 1. Table to store failed payment events
CREATE TABLE payment_failures (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  payment_id CHAR(36) NOT NULL, -- UUID stored as 36-character string
                                  user_id CHAR(36) NOT NULL,    -- UUID stored as 36-character string
                                  amount DECIMAL(10, 2) NOT NULL,
                                  currency VARCHAR(10) NOT NULL,
                                  reason TEXT,
                                  occurred_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  notified BOOLEAN DEFAULT FALSE
);

-- 2. Table to store log events from all services
CREATE TABLE service_logs (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              service_name VARCHAR(100) NOT NULL,
                              severity VARCHAR(10) NOT NULL, -- Must be 'Info', 'Warning', or 'Error'
                              message TEXT NOT NULL,
                              event_data JSON,               -- Use JSON instead of JSONB
                              occurred_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              notified BOOLEAN DEFAULT FALSE
);
