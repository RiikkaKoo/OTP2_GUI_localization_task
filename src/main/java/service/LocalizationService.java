package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalizationService {

    private Connection connection;
    private Map<String, String> strings = new HashMap<>();
    private Logger logger = Logger.getLogger(LocalizationService.class.getName());

    private void getConnection() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (Exception e) {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(e.getMessage());
            }
        }
    }

    public void loadStrings(String language) throws SQLException {
        Map<String, String> newStrings = new HashMap<>();
        try {
            getConnection();
            String query = "SELECT `key`, `value` FROM localization_strings WHERE language = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, language);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    newStrings.put(rs.getString("key"), rs.getString("value"));
                }
                if (newStrings.isEmpty()) {
                    throw new NullPointerException("No strings found");
                } else {
                    strings = newStrings;
                }
            } catch (Exception e) {
                if (logger.isLoggable(Level.INFO)) {
                    logger.info("Failed to load UI texts for language: " + language);
                }
                try {
                    loadEnglishStrings();
                } catch (Exception ex) {
                    throw new SQLException(e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    private void loadEnglishStrings() throws SQLException {
        Map<String, String> newStrings = new HashMap<>();
        String query = "SELECT `key`, `value` FROM localization_strings WHERE language = ?";
        try (
            PreparedStatement ps = connection.prepareStatement(query);) {
            ps.setString(1, "en");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                newStrings.put(rs.getString("key"), rs.getString("value"));
            }
            if (newStrings.isEmpty()) {
                throw new NullPointerException("No strings found");
            } else {
                strings = newStrings;
            }
        } catch (Exception ex) {
            newStrings.put("distance_label", "Distance (km)");
            newStrings.put("consumption_label", "Fuel Consumption (L/100 km)");
            newStrings.put("price_label", "Fuel Price (per liter)");
            newStrings.put("calculate_button", "Calculate Trip Cost");
            newStrings.put("result_label", "Total fuel needed: {0} L | Total cost: {1}");
            newStrings.put("invalid_input", "Invalid input");
            newStrings.put("results_saved", "Results saved to the database!");
            newStrings.put("connection_failed", "Database connection failed.");
            newStrings.put("save_failed", "Failed to save results to database.");
            strings = newStrings;
            throw new SQLException(ex.getMessage());
        }
    }

    public String getString(String key) {
        return strings.get(key);
    }

    public List<String> getAllKeys() {
        List<String> keys = new ArrayList<>();
        for (String key : strings.keySet()) {
            keys.add(key);
        }
        return keys;
    }
}
