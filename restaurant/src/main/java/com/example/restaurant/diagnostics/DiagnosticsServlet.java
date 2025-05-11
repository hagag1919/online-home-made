package com.example.restaurant.diagnostics;

import com.example.restaurant.utilities.EnvLoader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * A diagnostic servlet for troubleshooting database and environment variable issues
 */
@WebServlet("/diagnostics")
public class DiagnosticsServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(DiagnosticsServlet.class.getName());
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        
        out.println("<html><head><title>Diagnostics</title></head><body>");
        out.println("<h1>Environment & Database Diagnostics</h1>");
        
        // Environment information
        out.println("<h2>Environment Information</h2>");
        out.println("<ul>");
        out.println("<li>Working directory: " + new File(".").getAbsolutePath() + "</li>");
        
        String envPath = "/home/bakry/Bakry/FCAI/Semester_8/distributedSystems/assignments/online-home-made/restaurant";
        File envFile = new File(envPath + "/.env");
        out.println("<li>.env file exists at " + envFile.getAbsolutePath() + ": " + envFile.exists() + "</li>");
        
        // Check .env file variables
        String[] keysToCheck = {"DB_URL", "DB_USERNAME", "DB_PASSWORD", "DB_DRIVER"};
        out.println("<h3>.env File Variables</h3>");
        out.println("<ul>");
        for (String key : keysToCheck) {
            String value = EnvLoader.get(key);
            out.println("<li>" + key + ": " + 
                        (key.equals("DB_PASSWORD") ? 
                         (value != null && !value.isEmpty() ? "******** (set)" : "NOT SET") : 
                         (value != null ? value : "NOT SET")) + 
                        "</li>");
        }
        out.println("</ul>");
        
        // Try database connection
        out.println("<h2>Database Connection Test</h2>");
        String url = EnvLoader.get("DB_URL");
        String username = EnvLoader.get("DB_USERNAME");
        String password = EnvLoader.get("DB_PASSWORD");
        String driver = EnvLoader.get("DB_DRIVER");
        
        try {
            // Load the driver
            Class.forName(driver);
            out.println("<p>✅ Driver loaded successfully: " + driver + "</p>");
            
            try {
                // Try connection
                Connection connection = DriverManager.getConnection(url, username, password);
                out.println("<p>✅ Database connection successful!</p>");
                connection.close();
            } catch (SQLException e) {
                out.println("<p>❌ Failed to connect to database: " + e.getMessage() + "</p>");
                out.println("<p>SQLException State: " + e.getSQLState() + "</p>");
                out.println("<p>SQLException ErrorCode: " + e.getErrorCode() + "</p>");
            }
        } catch (ClassNotFoundException e) {
            out.println("<p>❌ Database driver not found: " + e.getMessage() + "</p>");
        }
        
        // Troubleshooting steps
        out.println("<h2>Troubleshooting Steps</h2>");
        out.println("<ol>");
        out.println("<li>Verify that the MySQL server is running</li>");
        out.println("<li>Confirm that user 'bakry' has proper privileges on the database</li>");
        out.println("<li>Try running: <code>mysql -u bakry -p</code> to test login</li>");
        out.println("<li>Check that the database 'restaurant_db' exists</li>");
        out.println("<li>Verify the MySQL user has proper network access (localhost)</li>");
        out.println("</ol>");
        
        out.println("</body></html>");
    }
}
