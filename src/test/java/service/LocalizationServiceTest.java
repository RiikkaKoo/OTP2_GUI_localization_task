package service;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocalizationServiceTest {

    private static Connection connection;
    private LocalizationService service;

    @BeforeAll
    static void setupDatabase() throws Exception {
        connection = DatabaseConnection.getConnection();

        Statement stmt = connection.createStatement();

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS localization_strings (
                id INT AUTO_INCREMENT PRIMARY KEY,
                `key` VARCHAR(255),
                `value` VARCHAR(255),
                language VARCHAR(10)
            )
        """);

        // Insert EN data
        stmt.execute("""
            INSERT INTO localization_strings (`key`, `value`, language) VALUES
            ('distance_label', 'Distance', 'en'),
            ('invalid_input', 'Invalid input', 'en')
        """);

        // Insert FR data
        stmt.execute("""
            INSERT INTO localization_strings (`key`, `value`, language) VALUES
            ('distance_label', 'Distance FR', 'fr')
        """);
    }

    @BeforeEach
    void setUp() {
        service = new LocalizationService();
    }

    // ✅ 1. Normal case
    @Test
    void loadStrings_validLanguage_loadsCorrectValues() throws Exception {
        service.loadStrings("en");

        assertEquals("Distance", service.getString("distance_label"));
        assertEquals("Invalid input", service.getString("invalid_input"));
    }

    @Test
    void loadStrings_differentLanguage_changesValues() throws Exception {
        service.loadStrings("fr");

        assertEquals("Distance FR", service.getString("distance_label"));
    }

    @Test
    void loadStrings_missingLanguage_fallsBackToEnglish() throws Exception {
        service.loadStrings("xx");

        assertEquals("Distance", service.getString("distance_label"));
    }

    @Test
    void getAllKeys_returnsAllKeys() throws Exception {
        service.loadStrings("en");

        List<String> keys = service.getAllKeys();

        assertTrue(keys.contains("distance_label"));
        assertTrue(keys.contains("invalid_input"));
    }

    @Test
    void getString_missingKey_returnsNull() throws Exception {
        service.loadStrings("en");

        assertNull(service.getString("non_existing"));
    }
}
