import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class TripCalculatorController {

    private Locale currentLocale = new Locale("en", "UK");

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
        ResourceBundle rb = ResourceBundle.getBundle("MessagesBundle", currentLocale);
        try {
            double totalFuel = ((Double.parseDouble(txtConsumption.getText()) / 100) * Double.parseDouble(txtDistance.getText()));
            double totalCost = (totalFuel * Double.parseDouble(txtPrice.getText()));
            String message = MessageFormat.format(
                    rb.getString("result.label"), totalFuel, totalCost
            );
            lblResult.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
            lblResult.setText(rb.getString("invalid.input"));
        }
    }

    @FXML
    private void onENClick(){
        setLanguage(new Locale("en", "UK"));
    }

    @FXML
    private void onFRClick(){
        setLanguage(new Locale("fr", "FR"));
    }

    @FXML
    private void onJPClick(){
        setLanguage(new Locale("ja", "JP"));
    }

    @FXML
    private void onIRClick(){
        setLanguage(new Locale("fa", "IR"));
    }

    private void setLanguage(Locale locale) {
        currentLocale = locale;
        updateTexts();
    }

    private void updateTexts() {
        ResourceBundle rb = ResourceBundle.getBundle("MessagesBundle", currentLocale);
        lblDistance.setText(rb.getString("distance.label"));
        lblConsumption.setText(rb.getString("consumption.label"));
        lblPrice.setText(rb.getString("price.label"));
        btnCalculate.setText(rb.getString("calculate.button"));
        lblResult.setText("");
    }

}
