package service;

import model.CalculationRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class CalculationServiceTest {

    @BeforeAll
    static void setupDatabase() throws Exception {
        Connection conn = DatabaseConnection.getConnection();

        Statement stmt = conn.createStatement();

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS calculation_records (
                 id INT AUTO_INCREMENT PRIMARY KEY,
                 distance DOUBLE NOT NULL,
                 consumption DOUBLE NOT NULL,
                 price DOUBLE NOT NULL,
                 total_fuel DOUBLE NOT NULL,
                 total_cost DOUBLE NOT NULL,
                 language VARCHAR(10),
                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
             )
        """);
    }

    @Test
    void saveCalculation_insertsData() throws Exception {
        CalculationService service = new CalculationService();

        CalculationRecord calculationRecord = new CalculationRecord(
                100, 5, 2, 5, 10, "EN"
        );

        service.saveCalculation(calculationRecord);

        Connection conn = DatabaseConnection.getConnection();
        var rs = conn.createStatement()
                .executeQuery("SELECT * FROM calculation_records");

        assertTrue(rs.next());
        assertEquals(100, rs.getDouble("distance"));
    }
}
