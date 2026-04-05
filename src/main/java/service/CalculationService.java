package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.CalculationRecord;

public class CalculationService {

    private Connection connection;

    public CalculationService() {
    }

    private void getConnection() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveCalculation(CalculationRecord record) throws SQLException {
        try {
            getConnection();
            String insert = "INSERT INTO calculation_records (distance, consumption, price, total_fuel, total_cost, language) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(insert);
            ps.setDouble(1, record.getDistance());
            ps.setDouble(2, record.getConsumption());
            ps.setDouble(3, record.getPrice());
            ps.setDouble(4, record.getTotalFuel());
            ps.setDouble(5, record.getTotalCost());
            ps.setString(6, record.getLanguage());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Could not save record: " + e.getMessage());
        }
    }
}
