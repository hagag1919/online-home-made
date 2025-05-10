-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS OrdersDB;

-- Select the database to use
USE OrdersDB;

-- Now create your tables
CREATE TABLE Restaurant (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL
);

CREATE TABLE Dish (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      price DECIMAL(10,2) NOT NULL,
                      amount INT NOT NULL,
                      description TEXT,
                      restaurant_id INT,
                      FOREIGN KEY (restaurant_id) REFERENCES Restaurant(id)
);
