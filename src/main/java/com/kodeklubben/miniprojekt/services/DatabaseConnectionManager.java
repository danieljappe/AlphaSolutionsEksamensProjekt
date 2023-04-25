package com.kodeklubben.miniprojekt.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManager {
    private final String url;
    private final Properties properties;

    public DatabaseConnectionManager(String host, String username, String password) {
        this.url = "jdbc:mysql://" + host;
        this.properties = new Properties();
        this.properties.setProperty("user", username);
        this.properties.setProperty("password", password);
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(this.url, this.properties);
        } catch (ClassNotFoundException error) {
            error.printStackTrace();
            throw new RuntimeException(error);
        }
    }
}
