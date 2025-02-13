package application.dormtrackr.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DtPaymentController implements Initializable {

    @FXML
    private Button homeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeButton.setOnAction(event -> loadScene("/application/dormtrackr/dashboard.fxml"));
    }

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane newSceneRoot = loader.load();
            Scene newScene = new Scene(newSceneRoot, 900, 560);
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}