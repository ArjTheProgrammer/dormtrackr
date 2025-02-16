package application.dormtrackr.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Date;
import java.time.LocalDate;

public class Payment {
    private final SimpleIntegerProperty paymentId;
    private final SimpleIntegerProperty dormerId;
    private final SimpleObjectProperty<LocalDate> paymentDate;

    public Payment() {
        this.paymentId = new SimpleIntegerProperty();
        this.dormerId = new SimpleIntegerProperty();
        this.paymentDate = new SimpleObjectProperty<>();
    }

    public Payment(int dormerId, LocalDate paymentDate) {
        this.paymentId = new SimpleIntegerProperty();
        this.dormerId = new SimpleIntegerProperty(dormerId);
        this.paymentDate = new SimpleObjectProperty<>(paymentDate);
    }

    public Payment(int paymentId, int dormerId, LocalDate paymentDate) {
        this.paymentId = new SimpleIntegerProperty(paymentId);
        this.dormerId = new SimpleIntegerProperty(dormerId);
        this.paymentDate = new SimpleObjectProperty<>(paymentDate);
    }

    public int getPaymentId() {
        return paymentId.get();
    }

    public void setPaymentId(int paymentId) {
        this.paymentId.set(paymentId);
    }

    public SimpleIntegerProperty paymentIdProperty() {
        return paymentId;
    }

    public int getDormerId() {
        return dormerId.get();
    }

    public void setDormerId(int dormerId) {
        this.dormerId.set(dormerId);
    }

    public SimpleIntegerProperty dormerIdProperty() {
        return dormerId;
    }

    public LocalDate getPaymentDate() {
        return paymentDate.get();
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate.set(paymentDate);
    }

    public SimpleObjectProperty<LocalDate> paymentDateProperty() {
        return paymentDate;
    }

    public Date getSqlDate() {
        return Date.valueOf(getPaymentDate());
    }
}