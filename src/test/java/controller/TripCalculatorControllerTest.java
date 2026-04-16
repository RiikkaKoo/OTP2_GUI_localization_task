package controller;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.junit.jupiter.api.*;
import service.CalculationService;
import service.DatabaseConnection;
import service.LocalizationService;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class TripCalculatorControllerTest {

    private TripCalculatorController controller;

    @BeforeAll
    static void setupDatabase() throws Exception {
        // Initialize JavaFX toolkit
        new JFXPanel();

        Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();

        // Create tables for tests
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS calculation_records (
                id INT AUTO_INCREMENT PRIMARY KEY,
                distance DOUBLE,
                consumption DOUBLE,
                price DOUBLE,
                total_fuel DOUBLE,
                total_cost DOUBLE,
                language VARCHAR(10)
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS localization_strings (
                id INT AUTO_INCREMENT PRIMARY KEY,
                `key` VARCHAR(255),
                `value` VARCHAR(255),
                language VARCHAR(10)
            )
        """);

        // Insert test localization data
        stmt.execute("INSERT INTO localization_strings (`key`, `value`, language) VALUES " +
                "('invalid_input', 'Invalid input', 'en')," +
                "('result_label', 'Fuel: {0} | Cost: {1}', 'en')," +
                "('results_saved', 'Saved!', 'en')," +
                "('save_failed', 'Save failed', 'en')," +
                "('connection_failed', 'Connection error', 'en')," +
                "('distance_label', 'Distance', 'en')," +
                "('consumption_label', 'Consumption', 'en')," +
                "('price_label', 'Price', 'en')," +
                "('calculate_button', 'Calculate', 'en')"
        );
    }

    @BeforeEach
    void initController() {
        controller = new TripCalculatorController();

        // Initialize real services
        controller.calculationService = new CalculationService();
        controller.localizationService = new LocalizationService();

        // Initialize JavaFX components
        controller.rootBox = new AnchorPane();
        controller.lblInfo = new Label();
        controller.lblResult = new Label();
        controller.lblDistance = new Label();
        controller.lblConsumption = new Label();
        controller.lblPrice = new Label();
        controller.txtDistance = new TextField();
        controller.txtConsumption = new TextField();
        controller.txtPrice = new TextField();
        controller.btnCalculate = new Button();

        controller.initialize();
    }

    @Test
    void calculate_validInput_savesToDatabase() throws Exception {
        controller.txtDistance.setText("100");
        controller.txtConsumption.setText("5");
        controller.txtPrice.setText("2");

        // Run calculation
        Platform.runLater(controller::calculate);
        Thread.sleep(200); // allow JavaFX thread to execute

        // Check result label
        assertTrue(controller.lblResult.getText().contains("Fuel"));
        assertTrue(controller.lblResult.getText().contains("Cost"));

        // Check info label
        assertEquals("Saved!", controller.lblInfo.getText());

        // Verify record exists in DB
        Connection conn = DatabaseConnection.getConnection();
        var rs = conn.createStatement().executeQuery("SELECT * FROM calculation_records");
        assertTrue(rs.next());
        assertEquals(100.0, rs.getDouble("distance"));
    }

    @Test
    void calculate_invalidInput_showsError() throws Exception {
        controller.txtDistance.setText("0");
        controller.txtConsumption.setText("5");
        controller.txtPrice.setText("2");

        Platform.runLater(controller::calculate);
        Thread.sleep(500);

        assertEquals("Invalid input", controller.lblResult.getText());
        assertEquals("", controller.lblInfo.getText());
    }

    @Test
    void changeLanguage() {
        LocalizationService localizationService = new LocalizationService();

        controller.onENClick();
        assertFalse(controller.isRTL);
        assertEquals("en", controller.currentLocale.getLanguage());
        assertEquals("GB", controller.currentLocale.getCountry());

        controller.onIRClick();
        assertTrue(controller.isRTL);
        assertEquals("fa", controller.currentLocale.getLanguage());
        assertEquals("IR", controller.currentLocale.getCountry());

        controller.onFRClick();
        assertFalse(controller.isRTL);
        assertEquals("fr", controller.currentLocale.getLanguage());
        assertEquals("FR", controller.currentLocale.getCountry());

        controller.onJPClick();
        assertFalse(controller.isRTL);
        assertEquals("ja", controller.currentLocale.getLanguage());
        assertEquals("JP", controller.currentLocale.getCountry());
    }
}
