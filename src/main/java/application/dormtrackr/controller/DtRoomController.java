package application.dormtrackr.controller;

import application.dormtrackr.model.Dormer;
import application.dormtrackr.model.Room;
import application.dormtrackr.model.dao.RoomDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DtRoomController implements Initializable {

    @FXML
    private Button homeButton;

    @FXML
    private TableColumn<Room, Integer> roomFloor;

    @FXML
    private TableColumn<Room, Integer> roomId;

    @FXML
    private TableColumn<Room, Integer> roomMaxCapacity;

    @FXML
    private TableColumn<Room, String> roomNum;

    @FXML
    private TableColumn<Room, Integer> roomOccupancy;

    @FXML
    private TableView<Room> roomTable;

    @FXML
    private Label viewDormer1;

    @FXML
    private Label viewDormer2;

    @FXML
    private Label viewDormer3;

    @FXML
    private Label viewDormer4;

    @FXML
    private Label viewRoomFloor;

    @FXML
    private Label viewRoomNum;

    private RoomDAO roomDAO;
    private int index;
    private Label[] labels;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roomDAO = new RoomDAO();
         labels = new Label[]{viewDormer1, viewDormer2, viewDormer3, viewDormer4};
        homeButton.setOnAction(event -> loadScene("/application/dormtrackr/dashboard.fxml"));
        viewRoomTable();
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

    private void viewRoomTable() {
        ObservableList<Room> rooms = roomDAO.getRooms();
        System.out.println("Number of rooms: " + rooms.size());
        roomTable.setItems(rooms);
        roomId.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getRoomId()));
        roomNum.setCellValueFactory(f -> f.getValue().roomNumberProperty());
        roomFloor.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getFloorNumber()));
        roomMaxCapacity.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getMaxCapacity()));
        roomOccupancy.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getCurrentOccupancy()));

        roomTable.setRowFactory(tv -> {
            TableRow<Room> myRow = new TableRow<>();
            myRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
                    index = roomTable.getSelectionModel().getSelectedIndex();

                    viewRoomNum.setText(String.valueOf(roomTable.getItems().get(index).getRoomNumber()));
                    viewRoomFloor.setText(String.valueOf(roomTable.getItems().get(index).getFloorNumber()));

                    String[] dormerList = roomTable.getItems().get(index).getDormers();

                    for (int i = 0; i < labels.length; i++){
                        if (i < dormerList.length){
                            labels[i].setText(dormerList[i]);
                        }else{
                            labels[i].setText("");
                        }
                    }
                }
            });
            return myRow;
        });
    }
}