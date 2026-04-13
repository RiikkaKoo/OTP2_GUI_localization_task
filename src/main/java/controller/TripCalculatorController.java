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
import java.util.Locale;

public class TripCalculatorController {

    protected CalculationService calculationService = new CalculationService();
    protected LocalizationService localizationService = new LocalizationService();
    protected Locale currentLocale = new Locale("en", "GB");
    protected boolean isRTL = false;

    @FXML protected AnchorPane rootBox;
    @FXML protected Label lblInfo;
    @FXML protected Label lblResult;
    @FXML protected Label lblDistance;
    @FXML protected Label lblConsumption;
    @FXML protected Label lblPrice;

    @FXML protected TextField txtDistance;
    @FXML protected TextField txtConsumption;
    @FXML protected TextField txtPrice;

    @FXML protected Button btnCalculate;

    @FXML
    public void initialize() {
        // Set initial language
        setLanguage(currentLocale);

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
    protected void calculate() {
        String invalid = localizationService.getString("invalid_input");
        try {

            if (txtDistance.getText().isEmpty() || txtConsumption.getText().isEmpty() || txtPrice.getText().isEmpty()) {
                lblResult.setText(invalid);
                return;
            }

            double distance = Double.parseDouble(txtDistance.getText());
            double consumption = Double.parseDouble(txtConsumption.getText());
            double price = Double.parseDouble(txtPrice.getText());

            if (distance <= 0 || consumption <= 0 || price <= 0) {
                lblResult.setText(invalid);
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
            }

        } catch (Exception e) {
            lblResult.setText(invalid);
        }
    }

    @FXML
    protected void onENClick(){
        this.isRTL = false;
        setLanguage(new Locale("en", "GB"));
    }

    @FXML
    protected void onFRClick(){
        this.isRTL = false;
        setLanguage(new Locale("fr", "FR"));
    }

    @FXML
    protected void onJPClick(){
        this.isRTL = false;
        setLanguage(new Locale("ja", "JP"));
    }

    @FXML
    protected void onIRClick(){
        this.isRTL = true;
        setLanguage(new Locale("fa", "IR"));
    }

    protected void setLanguage(Locale locale) {
        try {
            currentLocale = locale;
            localizationService.loadStrings(currentLocale.getLanguage());
            updateTexts();
            updateTextDirection();
        } catch (Exception e) {
            displayConnectionError();
        }
    }

    protected void updateTexts() {
        lblDistance.setText(localizationService.getString("distance_label"));
        lblConsumption.setText(localizationService.getString("consumption_label"));
        lblPrice.setText(localizationService.getString("price_label"));
        btnCalculate.setText(localizationService.getString("calculate_button"));
        lblResult.setText("");
        lblInfo.setText("");
        }

    protected void updateTextDirection() {

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
