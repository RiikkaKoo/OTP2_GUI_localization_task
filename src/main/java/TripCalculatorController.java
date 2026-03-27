import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import service.LocalizationService;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class TripCalculatorController {

    private Locale currentLocale = new Locale("en", "GB");
    private boolean isRTL = false;
    private Map<String, String> uiTexts;

    @FXML private AnchorPane rootBox;
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

        // Add listeners to clear result when input changes
        txtDistance.textProperty().addListener((obs, oldVal, newVal) -> lblResult.setText(""));
        txtConsumption.textProperty().addListener((obs, oldVal, newVal) -> lblResult.setText(""));
        txtPrice.textProperty().addListener((obs, oldVal, newVal) -> lblResult.setText(""));
    }

    @FXML
    private void calculate() {
        try {

            if (txtDistance.getText().isEmpty() || txtConsumption.getText().isEmpty() || txtPrice.getText().isEmpty()) {
                lblResult.setText(uiTexts.get("invalid.input"));
                return;
            }

            double distance = Double.parseDouble(txtDistance.getText());
            double consumption = Double.parseDouble(txtConsumption.getText());
            double price = Double.parseDouble(txtPrice.getText());

            if (distance <= 0 || consumption <= 0 || price <= 0) {
                lblResult.setText(uiTexts.get("invalid.input"));
                return;
            }

            double totalFuel = (consumption / 100) * distance;
            double totalCost = (totalFuel * price);

            NumberFormat costFormat = NumberFormat.getCurrencyInstance(currentLocale);
            NumberFormat numberFormat = NumberFormat.getNumberInstance(currentLocale);

            String message = MessageFormat.format(
                    uiTexts.get("result.label"), numberFormat.format(totalFuel), costFormat.format(totalCost)
            );
            lblResult.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
            lblResult.setText(uiTexts.get("invalid.input"));
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
        uiTexts = LocalizationService.getLocalizedStrings(currentLocale);
        Platform.runLater(() -> {
            updateTexts();
            updateTextDirection();
        });
    }

    private void updateTexts() {
        lblDistance.setText(uiTexts.get("distance.label"));
        lblConsumption.setText(uiTexts.get("consumption.label"));
        lblPrice.setText(uiTexts.get("price.label"));
        btnCalculate.setText(uiTexts.get("calculate.button"));
        lblResult.setText("");
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

}
