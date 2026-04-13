package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.CalculationRecord;

public class CalculationService {

    private Connection connection;
    private Logger logger = Logger.getLogger(CalculationService.class.getName());

    private void getConnection() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (Exception e) {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(e.getMessage());
            }
        }
    }

    public void saveCalculation(CalculationRecord calcRecord) throws SQLException {
        String insert = "INSERT INTO calculation_records (distance, consumption, price, total_fuel, total_cost, language) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            getConnection();
            try (PreparedStatement ps = connection.prepareStatement(insert);) {
                ps.setDouble(1, calcRecord.getDistance());
                ps.setDouble(2, calcRecord.getConsumption());
                ps.setDouble(3, calcRecord.getPrice());
                ps.setDouble(4, calcRecord.getTotalFuel());
                ps.setDouble(5, calcRecord.getTotalCost());
                ps.setString(6, calcRecord.getLanguage());
                ps.executeUpdate();
            } catch (Exception e) {
                throw new SQLException("Could not save record: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
}
