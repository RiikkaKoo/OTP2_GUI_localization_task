package service;

import controller.TripCalculatorController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class LocalizationService {

    private Connection connection;
    private Map<String, String> strings = new HashMap<>();

    public LocalizationService() {
        getConnection();
    }

    private void getConnection() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            TripCalculatorController calculatorController = new TripCalculatorController();
            calculatorController.displayConnectionError();
        }
    }

    public void loadStrings(String language) {
        Map<String, String> newStrings = new HashMap<>();
        try {
            String query = "SELECT `key`, value FROM localization_strings WHERE language = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, language);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                newStrings.put(rs.getString("key"), rs.getString("value"));
            }
            strings = newStrings;
        } catch (Exception e) {
            System.err.println("Failed to load UI texts for language: " + language);
            try {
                String query = "SELECT `key`, value FROM localization_strings WHERE language = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, "en");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    newStrings.put(rs.getString("key"), rs.getString("value"));
                }
                strings = newStrings;
            } catch (Exception ex) {
                newStrings.put("distance_label", "Distance (km)");
                newStrings.put("consumption_label", "Fuel Consumption (L/100 km)");
                newStrings.put("price_label", "Fuel Price (per liter)");
                newStrings.put("calculate_button", "Calculate Trip Cost");
                newStrings.put("result_label", "Total fuel needed: {0} L | Total cost: {1}");
                newStrings.put("invalid_input", "Invalid input");
                newStrings.put("results_saved", "Results saved to the database!");
                strings = newStrings;
            }
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
