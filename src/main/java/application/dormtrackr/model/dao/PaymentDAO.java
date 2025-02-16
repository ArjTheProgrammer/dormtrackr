package application.dormtrackr.model.dao;

import application.dormtrackr.model.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class PaymentDAO {
    public String getPaidStudents(){
        return "2/4";
    }

    public ObservableList<Payment> getPayments(){
        return FXCollections.observableArrayList(
                new Payment(1, 1, LocalDate.of(2025, 1, 1)),
                new Payment(2, 1, LocalDate.of(2025, 2, 1)),
                new Payment(3, 2, LocalDate.of(2025, 1, 1)),
                new Payment(4, 2, LocalDate.of(2025, 2, 1)),
                new Payment(5, 3, LocalDate.of(2025, 1, 1)),
                new Payment(6, 3, LocalDate.of(2025, 2, 1)),
                new Payment(7, 4, LocalDate.of(2025, 1, 1)),
                new Payment(8, 4, LocalDate.of(2025, 2, 1))
        );
    }
}
