package com.bookie.dao;

import java.io.InputStream;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection instance;
    private Connection connection;
    private String url;
    private String username;
    private String password;

    private DbConnection() throws SQLException {
        try {
            // Load the database configuration from properties file or environment variables
            loadConfiguration();

            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish the connection
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            throw new SQLException(ex);
        }
    }

    private void loadConfiguration() {
        Properties properties = new Properties();

        // Load the properties file from the classpath
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                System.out.println("Properties file not found in classpath. Falling back to environment variables.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load from properties file or environment variables
        this.url = properties.getProperty("db.url", System.getenv("DB_URL"));
        this.username = properties.getProperty("db.username", System.getenv("DB_USERNAME"));
        this.password = properties.getProperty("db.password", System.getenv("DB_PASSWORD"));

        if (url == null || username == null || password == null) {
            throw new RuntimeException("Database configuration is missing.");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DbConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DbConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DbConnection();
        }
        return instance;
    }
}