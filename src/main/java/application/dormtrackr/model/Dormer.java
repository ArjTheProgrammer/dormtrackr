package application.dormtrackr.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Dormer {
    private final SimpleIntegerProperty dormerId;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty number;
    private final SimpleStringProperty email;
    private final SimpleIntegerProperty roomId;

    public Dormer(String firstName, String lastName, String number, String email, int roomId) {
        this.dormerId = new SimpleIntegerProperty();
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.number = new SimpleStringProperty(number);
        this.email = new SimpleStringProperty(email);
        this.roomId = new SimpleIntegerProperty(roomId);
    }

    public Dormer(int dormerId, String firstName, String lastName, String number, String email, int roomId) {
        this.dormerId = new SimpleIntegerProperty(dormerId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.number = new SimpleStringProperty(number);
        this.email = new SimpleStringProperty(email);
        this.roomId = new SimpleIntegerProperty(roomId);
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

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public String getNumber() {
        return number.get();
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public SimpleStringProperty numberProperty() {
        return number;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public int getRoomId() {
        return roomId.get();
    }

    public void setRoomId(int roomId) {
        this.roomId.set(roomId);
    }

    public SimpleIntegerProperty roomIdProperty() {
        return roomId;
    }
}