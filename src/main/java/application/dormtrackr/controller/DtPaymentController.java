package application.dormtrackr.controller;

import application.dormtrackr.model.Payment;
import application.dormtrackr.model.dao.PaymentDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
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
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DtPaymentController implements Initializable {


    @FXML
    private TableColumn<Payment, String> dormerName;

    @FXML
    private Button homeButton;

    @FXML
    private DatePicker inputDate;

    @FXML
    private TextField inputDormerId;

    @FXML
    private Spinner<Integer> monthSpinner;

    @FXML
    private TableColumn<Payment, LocalDate> paymentDate;

    @FXML
    private TableColumn<Payment, Integer> paymentId;

    @FXML
    private TableView<Payment> paymentTable;

    private PaymentDAO paymentDAO;


    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Spinner<Integer> yearSpinner;

    private int index;
    LocalDate currentDate = LocalDate.now();
    int currentMonth;
    int currentYear;
    private int id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paymentDAO = new PaymentDAO();
        currentMonth = currentDate.getMonthValue();
        currentYear = currentDate.getYear();

        SpinnerValueFactory<Integer> monthValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, currentMonth);
        monthSpinner.setValueFactory(monthValueFactory);
        monthSpinner.setEditable(true);

        SpinnerValueFactory<Integer> yearValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2023, Integer.MAX_VALUE, currentYear);
        yearSpinner.setValueFactory(yearValueFactory);
        yearSpinner.setEditable(true);

        statusComboBox.setItems(FXCollections.observableArrayList("Paid", "Unpaid"));
        statusComboBox.setValue("Paid");

        homeButton.setOnAction(event -> loadScene("/application/dormtrackr/dashboard.fxml"));
        viewPaymentTable();
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

    private void viewPaymentTable() {
        ObservableList<Payment> payments = paymentDAO.getPayments(statusComboBox.getValue(), monthSpinner.getValue(), yearSpinner.getValue());
        System.out.println("Number of payments: " + payments.size());
        paymentTable.setItems(payments);

        paymentId.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue().getPaymentId()));
        dormerName.setCellValueFactory(f -> f.getValue().dormerNameProperty());
        paymentDate.setCellValueFactory(f -> f.getValue().paymentDateProperty());

        paymentTable.setRowFactory(tv -> {
            TableRow<Payment> myRow = new TableRow<>();
            myRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
                    index = paymentTable.getSelectionModel().getSelectedIndex();

                    inputDormerId.setText(String.valueOf(paymentTable.getItems().get(index).getDormerId()));
                    inputDate.setValue(paymentTable.getItems().get(index).getPaymentDate());
                }
            });
            return myRow;
        });
    }

    @FXML
    void searchPayment(ActionEvent event) {
        viewPaymentTable();
    }

}