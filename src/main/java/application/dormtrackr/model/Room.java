package application.dormtrackr.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Room {
    private final SimpleIntegerProperty roomId;
    private final SimpleStringProperty roomNumber;
    private final SimpleIntegerProperty floorNumber;
    private final SimpleIntegerProperty maxCapacity;
    private final SimpleIntegerProperty currentOccupancy;
    private final String[] dormers;

    public Room(int roomId, String roomNumber, int floorNumber, int maxCapacity, int currentOccupancy, String[] dormers) {
        this.roomId = new SimpleIntegerProperty(roomId);
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.floorNumber = new SimpleIntegerProperty(floorNumber);
        this.maxCapacity = new SimpleIntegerProperty(maxCapacity);
        this.currentOccupancy = new SimpleIntegerProperty(currentOccupancy);
        this.dormers = dormers;
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

    public String getRoomNumber() {
        return roomNumber.get();
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber.set(roomNumber);
    }

    public SimpleStringProperty roomNumberProperty() {
        return roomNumber;
    }

    public int getFloorNumber() {
        return floorNumber.get();
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber.set(floorNumber);
    }

    public SimpleIntegerProperty floorNumberProperty() {
        return floorNumber;
    }

    public int getMaxCapacity() {
        return maxCapacity.get();
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity.set(maxCapacity);
    }

    public SimpleIntegerProperty maxCapacityProperty() {
        return maxCapacity;
    }

    public int getCurrentOccupancy() {
        return currentOccupancy.get();
    }

    public void setCurrentOccupancy(int currentOccupancy) {
        this.currentOccupancy.set(currentOccupancy);
    }

    public SimpleIntegerProperty currentOccupancyProperty() {
        return currentOccupancy;
    }

    public String[] getDormers() {
        return dormers;
    }
}