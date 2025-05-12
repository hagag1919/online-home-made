-- Create the database
CREATE DATABASE IF NOT EXISTS OrdersDB;
USE OrdersDB;

-- Create the Orders table
CREATE TABLE Orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        userID BIGINT NOT NULL,
                        restaurantID BIGINT NOT NULL,
                        totalPrice DECIMAL(10, 2) NOT NULL,
                        destination VARCHAR(255) NOT NULL,
                        shippingCompany VARCHAR(255) NOT NULL,
                        status VARCHAR(100) NOT NULL
);

-- Create the OrderDishes table
CREATE TABLE OrderDishes (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             name VARCHAR(255) NOT NULL,
                             amount INT NOT NULL,
                             price DECIMAL(10, 2) NOT NULL,
                             description TEXT,
                             CONSTRAINT fk_order
                                 FOREIGN KEY (order_id)
                                     REFERENCES Orders(id)
                                     ON DELETE CASCADE
);

ALTER TABLE Orders
    ADD COLUMN status VARCHAR(255) DEFAULT 'Pending';