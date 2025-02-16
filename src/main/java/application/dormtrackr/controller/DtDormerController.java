package application.dormtrackr.controller;

import application.dormtrackr.model.Dormer;
import application.dormtrackr.model.dao.DormerDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DtDormerController implements Initializable {

    @FXML
    private TableColumn<Dormer, String> dormerEmail;

    @FXML
    private TableColumn<Dormer, String> dormerFirstName;

    @FXML
    private TableColumn<Dormer, Integer> dormerId;

    @FXML
    private TableColumn<Dormer, String> dormerLastName;

    @FXML
    private TableColumn<Dormer, String> dormerNumber;

    @FXML
    private TableColumn<Dormer,Integer> dormerRoomId;

    @FXML
        private TableView<Dormer> dormerTable;

    @FXML
    private Button homeButton;

    @FXML
    private TextField inputEmail;

    @FXML
    private TextField inputFirstName;

    @FXML
    private TextField inputLastName;

    @FXML
    private TextField inputNumber;

    private DormerDAO dormerDAO;
    private int index;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dormerDAO = new DormerDAO();
        homeButton.setOnAction(event -> loadScene("/application/dormtrackr/dashboard.fxml"));
        viewDormerTable();
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

    private void viewDormerTable(){
        ObservableList<Dormer> dormers = dormerDAO.getDormers();
        System.out.println("Number of dormers:   " + dormers.size());
        dormerTable.setItems(dormers);
        dormerId.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getDormerId()));
        dormerFirstName.setCellValueFactory(f -> f.getValue().firstNameProperty());
        dormerLastName.setCellValueFactory(f -> f.getValue().lastNameProperty());
        dormerNumber.setCellValueFactory(f -> f.getValue().numberProperty());
        dormerEmail.setCellValueFactory(f -> f.getValue().emailProperty());
        dormerRoomId.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getRoomId()));

        dormerTable.setRowFactory( tv -> {
            TableRow<Dormer> myRow = new TableRow<>();
            myRow.setOnMouseClicked (event ->
            {
                if (event.getClickCount() == 1 && (!myRow.isEmpty()))
                {
                    index = dormerTable.getSelectionModel().getSelectedIndex();

                    inputFirstName.setText(dormerTable.getItems().get(index).getFirstName());
                    inputLastName.setText(dormerTable.getItems().get(index).getLastName());
                    inputNumber.setText(dormerTable.getItems().get(index).getNumber());
                    inputEmail.setText(dormerTable.getItems().get(index).getEmail());
                }
            });
            return myRow;
        });
    }
}