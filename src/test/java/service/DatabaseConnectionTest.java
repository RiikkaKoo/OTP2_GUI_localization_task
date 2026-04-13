package service;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    void testGetConnection_success() {
        try {
            Connection connection = DatabaseConnection.getConnection();

            assertNotNull(connection, "Connection should not be null");
            assertFalse(connection.isClosed(), "Connection should be open");

            connection.close();
        } catch (Exception e) {
            fail("Connection should not throw exception: " + e.getMessage());
        }
    }
}
