package application.dormtrackr.model.dao;

import application.dormtrackr.model.Dormer;
import application.dormtrackr.model.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomDAO extends BaseDAO<Room> {
    private static final String GET_ROOMS =
            "SELECT " +
                    "    r.room_id, " +
                    "    r.room_number, " +
                    "    r.floor_number, " +
                    "    r.max_capacity, " +
                    "    r.current_occupancy, " +
                    "    STRING_AGG(d.first_name + ' ' + d.last_name, ', ') AS dormers " +
                    "FROM " +
                    "    Room r " +
                    "LEFT JOIN " +
                    "    Dormer d ON r.room_id = d.room_id " +
                    "GROUP BY " +
                    "    r.room_id, r.room_number, r.floor_number, r.max_capacity, r.current_occupancy;";
    private static final String GET_AVAILABLE_ROOM = "SELECT COUNT(*) FROM Room WHERE current_occupancy != max_capacity";

    public String getAvailableRoom(){
            int total = 0;

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(GET_AVAILABLE_ROOM);
                 ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    total = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return "" + total;
    }

    public ObservableList<Room> getRooms(){
        ObservableList<Room> dormers = FXCollections.observableArrayList();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_ROOMS);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                dormers.add(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dormers;
    }

    private Room mapResultSetToRoom(ResultSet resultSet) throws SQLException {
        int roomId = resultSet.getInt("room_id");
        String roomNumber = resultSet.getString("room_number");
        int floorNumber = resultSet.getInt("floor_number");
        int maxCapacity = resultSet.getInt("max_capacity");
        int currentOccupancy = resultSet.getInt("current_occupancy");
        String dormersStr = resultSet.getString("dormers");

        String[] dormers = (dormersStr != null) ? dormersStr.split(", ") : new String[]{};

        return new Room(roomId, roomNumber, floorNumber, maxCapacity, currentOccupancy, dormers);
    }
}
