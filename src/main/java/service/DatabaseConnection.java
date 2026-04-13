package service;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    // Private constructor for this class
    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties");
            Properties prop = new Properties();
            prop.load(input);
            return DriverManager.getConnection(prop.getProperty("db.url"), prop.getProperty("db.rootUser"), prop.getProperty("db.rootPassword"));
        } catch (Exception e) {
            throw new SQLException("Failed to get connection: " + e.getMessage());
        }
    }
}

