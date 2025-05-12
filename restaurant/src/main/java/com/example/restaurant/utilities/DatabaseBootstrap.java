package com.example.restaurant.utilities;

import com.example.restaurant.models.Account;
import com.example.restaurant.models.Dish;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

/**
 * Utility class to initialize the database with sample data for testing
 */
@Singleton
@Startup
public class DatabaseBootstrap {
    
    private static final Logger LOGGER = Logger.getLogger(DatabaseBootstrap.class.getName());
    
    @PersistenceContext(unitName = "restaurantPU")
    private EntityManager entityManager;
    
    @PostConstruct
    @Transactional
    public void init() {
        try {
            // Check if database already has data
            Long accountCount = (Long) entityManager.createQuery("SELECT COUNT(a) FROM Account a").getSingleResult();
            
            if (accountCount == 0) {
                LOGGER.info("Initializing database with sample data");
                createSampleData();
                LOGGER.info("Sample data created successfully");
            } else {
                LOGGER.info("Database already contains data, skipping initialization");
            }
        } catch (Exception e) {
            LOGGER.severe("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Transactional
    public void createSampleData() {
        // Create restaurant accounts
        Account bakrysRestaurant = new Account("Bakry's Restaurant", "password123");
        entityManager.persist(bakrysRestaurant);
        
        Account cairoStreetFood = new Account("Cairo Street Food", "password456");
        entityManager.persist(cairoStreetFood);
        
        // Create dishes for Bakry's Restaurant
        Dish koshari = new Dish("Koshari", 50.00, 100, bakrysRestaurant);
        koshari.setDescription("Traditional Egyptian dish with rice, pasta, lentils, chickpeas, and tomato sauce");
        entityManager.persist(koshari);
        
        Dish mahshi = new Dish("Mahshi", 65.00, 50, bakrysRestaurant);
        mahshi.setDescription("Stuffed grape leaves and vegetables with rice and herbs");
        entityManager.persist(mahshi);
        
        Dish kofta = new Dish("Kofta", 75.00, 75, bakrysRestaurant);
        kofta.setDescription("Grilled minced meat with spices, served with rice and salad");
        entityManager.persist(kofta);
        
        // Create dishes for Cairo Street Food
        Dish taameya = new Dish("Taameya", 15.00, 200, cairoStreetFood);
        taameya.setDescription("Egyptian falafel made with fava beans");
        entityManager.persist(taameya);
        
        Dish hawawshi = new Dish("Hawawshi", 35.00, 80, cairoStreetFood);
        hawawshi.setDescription("Egyptian meat pie with minced beef, onions and spices");
        entityManager.persist(hawawshi);
        
        LOGGER.info("Created sample restaurants and dishes");
    }
}
