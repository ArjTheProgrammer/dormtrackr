package application.dormtrackr.controller;

import application.dormtrackr.model.Dormer;
import application.dormtrackr.model.dao.DormerDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
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

    @FXML
    private TextField inputRoomId;

    private DormerDAO dormerDAO;
    private int index;
    private int id;

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
                    id = dormerTable.getItems().get(index).getDormerId();
                    inputFirstName.setText(dormerTable.getItems().get(index).getFirstName());
                    inputLastName.setText(dormerTable.getItems().get(index).getLastName());
                    inputNumber.setText(dormerTable.getItems().get(index).getNumber());
                    inputEmail.setText(dormerTable.getItems().get(index).getEmail());
                    inputRoomId.setText("" + dormerTable.getItems().get(index).getRoomId());
                }
            });
            return myRow;
        });
    }

    @FXML
    void addDormer(ActionEvent event) {
        if (inputFirstName.getText().isEmpty() && inputLastName.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Names are blank");
            alert.show();
            return;
        }
        String firstName = inputFirstName.getText();
        String lastName = inputLastName.getText();
        String number = inputNumber.getText();
        String email = inputEmail.getText();
        int roomId = Integer.parseInt(inputRoomId.getText());

        System.out.println(firstName + lastName + number + email + roomId);
        Dormer dormer = new Dormer(firstName, lastName, number, email, roomId);

        if (dormerDAO.addDormer(dormer)){
            viewDormerTable();
            inputFirstName.setText("");
            inputLastName.setText("");
            inputNumber.setText("");
            inputEmail.setText("");
            inputRoomId.setText("");
        }
    }

    @FXML
    void updateDormer(ActionEvent event) {
        String firstName = inputFirstName.getText();
        String lastName = inputLastName.getText();
        String number = inputNumber.getText();
        String email = inputEmail.getText();
        int roomId = Integer.parseInt(inputRoomId.getText());

        System.out.println(firstName + lastName + number + email + roomId);
        Dormer dormer = new Dormer(id, firstName, lastName, number, email, roomId);

        if (dormerDAO.updateDormer(dormer)){
            viewDormerTable();
            inputFirstName.setText("");
            inputLastName.setText("");
            inputNumber.setText("");
            inputEmail.setText("");
            inputRoomId.setText("");
        }
    }
}