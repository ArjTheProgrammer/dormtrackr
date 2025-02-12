package application.dormtrackr;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Button dormerButton;

    @FXML
    private Button roomButton;

    @FXML
    private Button paymentButton;

    @FXML
    private Text tdText;

    @FXML
    private Text arText;

    @FXML
    private Text psText;

    @FXML
    private Label tdLabel;

    @FXML
    private Label arLabel;

    @FXML
    private Label psLabel;

    @FXML
    private void initialize() {
        dormerButton.setOnAction(event -> loadScene("dt-dormer.fxml"));
        roomButton.setOnAction(event -> loadScene("dt-room.fxml"));
        paymentButton.setOnAction(event -> loadScene("dt-payment.fxml"));
    }

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane newSceneRoot = loader.load();
            Scene newScene = new Scene(newSceneRoot, 900, 560);
            Stage stage = (Stage) dormerButton.getScene().getWindow();
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}