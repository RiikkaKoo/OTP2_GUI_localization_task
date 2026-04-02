package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.CalculationRecord;
import service.CalculationService;
import service.LocalizationService;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TripCalculatorController {

    private CalculationService calculationService = new CalculationService();
    private static LocalizationService localizationService = new LocalizationService();
    private Locale currentLocale = new Locale("en", "GB");
    private boolean isRTL = false;
    private List<String> allKeys;

    @FXML private AnchorPane rootBox;
    @FXML private Label lblInfo;
    @FXML private Label lblResult;
    @FXML private Label lblDistance;
    @FXML private Label lblConsumption;
    @FXML private Label lblPrice;

    @FXML private TextField txtDistance;
    @FXML private TextField txtConsumption;
    @FXML private TextField txtPrice;

    @FXML private Button btnCalculate;

    @FXML
    public void initialize() {
        // Set initial language
        setLanguage(currentLocale);
        allKeys = localizationService.getAllKeys();

        // Add listeners to clear result when input changes
        txtDistance.textProperty().addListener((obs, oldVal, newVal) -> {
            lblResult.setText("");
            lblInfo.setText("");
        });
        txtConsumption.textProperty().addListener((obs, oldVal, newVal) -> {
            lblResult.setText("");
            lblInfo.setText("");
        });
        txtPrice.textProperty().addListener((obs, oldVal, newVal) -> {
            lblResult.setText("");
            lblInfo.setText("");
        });
    }

    @FXML
    private void calculate() {
        try {

            if (txtDistance.getText().isEmpty() || txtConsumption.getText().isEmpty() || txtPrice.getText().isEmpty()) {
                lblResult.setText(localizationService.getString("invalid_input"));
                return;
            }

            double distance = Double.parseDouble(txtDistance.getText());
            double consumption = Double.parseDouble(txtConsumption.getText());
            double price = Double.parseDouble(txtPrice.getText());

            if (distance <= 0 || consumption <= 0 || price <= 0) {
                lblResult.setText(localizationService.getString("invalid_input"));
                return;
            }

            double totalFuel = (consumption / 100) * distance;
            double totalCost = (totalFuel * price);

            NumberFormat costFormat = NumberFormat.getCurrencyInstance(currentLocale);
            NumberFormat numberFormat = NumberFormat.getNumberInstance(currentLocale);

            String message = MessageFormat.format(
                    localizationService.getString("result_label"), numberFormat.format(totalFuel), costFormat.format(totalCost)
            );
            lblResult.setText(message);

            try {
                calculationService.saveCalculation(new CalculationRecord(distance, consumption, price, totalFuel, totalCost, currentLocale.getLanguage()));
                lblInfo.setText(localizationService.getString("results_saved"));
            } catch (Exception e) {
                lblInfo.setText(localizationService.getString("save_failed"));
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblResult.setText(localizationService.getString("invalid_input"));
        }
    }

    @FXML
    private void onENClick(){
        this.isRTL = false;
        setLanguage(new Locale("en", "GB"));
    }

    @FXML
    private void onFRClick(){
        this.isRTL = false;
        setLanguage(new Locale("fr", "FR"));
    }

    @FXML
    private void onJPClick(){
        this.isRTL = false;
        setLanguage(new Locale("ja", "JP"));
    }

    @FXML
    private void onIRClick(){
        this.isRTL = true;
        setLanguage(new Locale("fa", "IR"));
    }

    private void setLanguage(Locale locale) {
        currentLocale = locale;
        localizationService.loadStrings(currentLocale.getLanguage());
        Platform.runLater(() -> {
            updateTexts();
            updateTextDirection();
        });
    }

    private void updateTexts() {
        lblDistance.setText(localizationService.getString("distance_label"));
        lblConsumption.setText(localizationService.getString("consumption_label"));
        lblPrice.setText(localizationService.getString("price_label"));
        btnCalculate.setText(localizationService.getString("calculate_button"));
        lblResult.setText("");
        lblInfo.setText("");
        }

    private void updateTextDirection() {

        Platform.runLater(() -> {
            if (rootBox != null) {
                NodeOrientation orientation = isRTL
                        ? NodeOrientation.RIGHT_TO_LEFT
                        : NodeOrientation.LEFT_TO_RIGHT;

                rootBox.setNodeOrientation(orientation);
                txtDistance.setNodeOrientation(orientation);
                txtConsumption.setNodeOrientation(orientation);
                txtPrice.setNodeOrientation(orientation);
            }
        });
    }

    public void displayConnectionError() {
        lblInfo.setText(localizationService.getString("connection_failed"));
    }

}
