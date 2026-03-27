import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TripCalculatorApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main_view.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/style.css");
        scene.getRoot().setStyle("-fx-font-family: 'Noto Sans CJK JP', 'Noto Sans CJK', 'Noto Sans';");
        stage.setScene(scene);
        stage.show();
    }
}
