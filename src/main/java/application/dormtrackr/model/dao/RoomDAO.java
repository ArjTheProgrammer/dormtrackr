package application.dormtrackr.model.dao;

import application.dormtrackr.model.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RoomDAO {
    public String getAvailableRoom(){
        return "28";
    }

    public ObservableList<Room> getRooms(){
        return FXCollections.observableArrayList(
                new Room(1, "101", 1, 4, 2, new String[]{"Dormer1", "Dormer2"}),
                new Room(2, "102", 1, 4, 0, new String[]{}),
                new Room(3, "103", 1, 4, 0, new String[]{}),
                new Room(4, "104", 1, 4, 0, new String[]{}),
                new Room(5, "105", 1, 4, 0, new String[]{}),
                new Room(6, "106", 1, 4, 0, new String[]{}),
                new Room(7, "201", 2, 4, 0, new String[]{}),
                new Room(8, "202", 2, 4, 0, new String[]{}),
                new Room(9, "203", 2, 4, 0, new String[]{}),
                new Room(10, "204", 2, 4, 0, new String[]{}),
                new Room(11, "205", 2, 4, 0, new String[]{}),
                new Room(12, "206", 2, 4, 0, new String[]{}),
                new Room(13, "301", 3, 4, 0, new String[]{}),
                new Room(14, "302", 3, 4, 0, new String[]{}),
                new Room(15, "303", 3, 4, 0, new String[]{}),
                new Room(16, "304", 3, 4, 0, new String[]{}),
                new Room(17, "305", 3, 4, 0, new String[]{}),
                new Room(18, "306", 3, 4, 0, new String[]{}),
                new Room(19, "401", 4, 4, 0, new String[]{}),
                new Room(20, "402", 4, 4, 0, new String[]{}),
                new Room(21, "403", 4, 4, 0, new String[]{}),
                new Room(22, "404", 4, 4, 0, new String[]{}),
                new Room(23, "405", 4, 4, 0, new String[]{}),
                new Room(24, "406", 4, 4, 0, new String[]{}),
                new Room(25, "501", 5, 4, 0, new String[]{}),
                new Room(26, "502", 5, 4, 0, new String[]{}),
                new Room(27, "503", 5, 4, 0, new String[]{}),
                new Room(28, "504", 5, 4, 0, new String[]{}),
                new Room(29, "505", 5, 4, 0, new String[]{}),
                new Room(30, "506", 5, 4, 0, new String[]{})
        );
    }
}
