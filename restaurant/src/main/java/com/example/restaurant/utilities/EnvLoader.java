package com.example.restaurant.utilities;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import java.io.File;
import java.util.logging.Logger;

/**
 * Utility class to load environment variables from .env file
 */
@Singleton
@Startup
public class EnvLoader {
    
    private static final Logger LOGGER = Logger.getLogger(EnvLoader.class.getName());
    private static Dotenv dotenv;
    
    @PostConstruct
    public void init() {
        try {
            LOGGER.info("Loading environment variables from .env file");
            
            // Try to find the .env file in multiple locations
            File currentDir = new File(".");
            LOGGER.info("Current directory: " + currentDir.getAbsolutePath());
            
            String envPath = "/home/bakry/Bakry/FCAI/Semester_8/distributedSystems/assignments/online-home-made/restaurant";
            File envFile = new File(envPath + "/.env");
            
            if (envFile.exists()) {
                LOGGER.info(".env file found at: " + envFile.getAbsolutePath());
            } else {
                LOGGER.warning(".env file NOT found at: " + envFile.getAbsolutePath());
            }
            
            dotenv = Dotenv.configure()
                    .directory(envPath)
                    .ignoreIfMissing()
                    .load();
            
            LOGGER.info("Environment variables loaded successfully");
            
            // Check if variables were actually loaded
            String[] keysToCheck = {"DB_URL", "DB_USERNAME", "DB_PASSWORD", "DB_DRIVER"};
            for (String key : keysToCheck) {
                String value = dotenv.get(key);
                LOGGER.info(key + " found: " + (value != null && !value.isEmpty()));
            }
            
        } catch (Exception e) {
            LOGGER.severe("Failed to load environment variables: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get an environment variable
     *
     * @param key the environment variable name
     * @return the environment variable value
     */
    public static String get(String key) {
        if (dotenv == null) {
            // Lazily initialize if not already done
            try {
                LOGGER.info("Lazy loading of environment variables");
                dotenv = Dotenv.configure()
                        .directory("/home/bakry/Bakry/FCAI/Semester_8/distributedSystems/assignments/online-home-made/restaurant")
                        .ignoreIfMissing()
                        .load();
            } catch (Exception e) {
                LOGGER.severe("Failed to lazy load environment variables: " + e.getMessage());
            }
        }
        
        String value = dotenv.get(key);
        if (value == null || value.isEmpty()) {
            // Try system environment variables as fallback
            LOGGER.info("Value not found in .env file, trying system environment for key: " + key);
            value = System.getenv(key);
        }
        
        return value;
    }
}
