package com.example.restaurant.diagnostics;

import com.example.restaurant.utilities.EnvLoader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A servlet for testing database queries and displaying results
 */
@WebServlet("/db-test")
public class DatabaseTestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        
        out.println("<html><head><title>Database Test</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println("h1, h2 { color: #333; }");
        out.println("pre { background-color: #f5f5f5; padding: 10px; border-radius: 5px; overflow-x: auto; }");
        out.println("table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("tr:nth-child(even) { background-color: #f9f9f9; }");
        out.println("form { margin: 20px 0; padding: 15px; background-color: #f5f5f5; border-radius: 5px; }");
        out.println("textarea { width: 100%; height: 150px; margin: 10px 0; font-family: monospace; }");
        out.println("input[type=submit] { padding: 10px 15px; background-color: #4CAF50; color: white; border: none; cursor: pointer; }");
        out.println("</style>");
        out.println("</head><body>");
        
        out.println("<h1>Database Test Console</h1>");
        
        // Database information
        String url = EnvLoader.get("DB_URL");
        String username = EnvLoader.get("DB_USERNAME");
        String password = EnvLoader.get("DB_PASSWORD");
        
        out.println("<h2>Database Connection</h2>");
        out.println("<ul>");
        out.println("<li>URL: " + url + "</li>");
        out.println("<li>Username: " + username + "</li>");
        out.println("</ul>");
        
        // Query form
        out.println("<h2>Execute SQL Query</h2>");
        out.println("<form method='post' action='db-test'>");
        out.println("<div><textarea name='query' placeholder='Enter your SQL query here...'>" + getDefaultQuery() + "</textarea></div>");
        out.println("<div><input type='submit' value='Execute Query'></div>");
        out.println("</form>");
        
        // Sample queries
        out.println("<h2>Sample Queries</h2>");
        out.println("<pre>SHOW DATABASES;</pre>");
        out.println("<pre>SHOW TABLES;</pre>");
        out.println("<pre>DESCRIBE your_table_name;</pre>");
        out.println("<pre>SELECT * FROM your_table_name LIMIT 10;</pre>");
        out.println("<pre>SELECT version(), database();</pre>");
        
        // Display some basic database info
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {
            
            // Show MySQL version and current database
            out.println("<h2>Database Information</h2>");
            try (ResultSet rs = stmt.executeQuery("SELECT version() as version, database() as db")) {
                if (rs.next()) {
                    out.println("<ul>");
                    out.println("<li>MySQL Version: " + rs.getString("version") + "</li>");
                    out.println("<li>Current Database: " + rs.getString("db") + "</li>");
                    out.println("</ul>");
                }
            }
            
            // Show tables in the current database
            out.println("<h2>Tables in Current Database</h2>");
            try (ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
                boolean hasTables = false;
                out.println("<ul>");
                while (rs.next()) {
                    hasTables = true;
                    out.println("<li>" + rs.getString(1) + "</li>");
                }
                if (!hasTables) {
                    out.println("<li>No tables found in the current database</li>");
                }
                out.println("</ul>");
            }
            
        } catch (SQLException e) {
            out.println("<h2>Error</h2>");
            out.println("<pre>" + e.getMessage() + "</pre>");
        }
        
        out.println("</body></html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("query");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        
        out.println("<html><head><title>Query Result</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println("h1, h2 { color: #333; }");
        out.println("pre { background-color: #f5f5f5; padding: 10px; border-radius: 5px; overflow-x: auto; }");
        out.println("table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("tr:nth-child(even) { background-color: #f9f9f9; }");
        out.println(".back-link { margin-top: 20px; }");
        out.println("</style>");
        out.println("</head><body>");
        
        out.println("<h1>Query Result</h1>");
        out.println("<h2>Executed Query:</h2>");
        out.println("<pre>" + query + "</pre>");
        
        String url = EnvLoader.get("DB_URL");
        String username = EnvLoader.get("DB_USERNAME");
        String password = EnvLoader.get("DB_PASSWORD");
        
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {
            
            boolean hasResultSet = stmt.execute(query);
            
            if (hasResultSet) {
                try (ResultSet rs = stmt.getResultSet()) {
                    // Get column count
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    
                    // Start the table
                    out.println("<table>");
                    
                    // Table header
                    out.println("<tr>");
                    for (int i = 1; i <= columnCount; i++) {
                        out.println("<th>" + metaData.getColumnName(i) + "</th>");
                    }
                    out.println("</tr>");
                    
                    // Table data
                    while (rs.next()) {
                        out.println("<tr>");
                        for (int i = 1; i <= columnCount; i++) {
                            out.println("<td>" + rs.getString(i) + "</td>");
                        }
                        out.println("</tr>");
                    }
                    
                    out.println("</table>");
                }
            } else {
                int updateCount = stmt.getUpdateCount();
                out.println("<p>Query executed successfully. Rows affected: " + updateCount + "</p>");
            }
            
        } catch (SQLException e) {
            out.println("<h2>Error</h2>");
            out.println("<pre>" + e.getMessage() + "</pre>");
        }
        
        out.println("<div class='back-link'><a href='db-test'>Back to Query Console</a></div>");
        out.println("</body></html>");
    }
    
    private String getDefaultQuery() {
        return "SHOW TABLES;";
    }
}
