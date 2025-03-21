package application.dormtrackr.model.dao;

import application.dormtrackr.model.Dormer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;

public class DormerDAO extends BaseDAO<Dormer> {

    private static final String GET_ALL_DORMERS = "SELECT * FROM Dormer";
    private static final String GET_FILTERED_DORMERS = "SELECT * FROM Dormer WHERE first_name LIKE ? OR last_name LIKE ?";
    private static final String COUNT_ROWS = "SELECT COUNT(*) AS total FROM Dormer";
    private static final String INSERT_DORMER =
            "INSERT INTO Dormer (first_name, last_name, contact_number, email, room_id) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_DORMER =
            "UPDATE Dormer SET first_name=?, last_name=?, contact_number=?, email=?, room_id=? WHERE dormer_id=?";

    private static final String DELETE_DORMER =
            "DELETE FROM Dormer WHERE dormer_id=?";

    private static final String INCREASE_OCCUPANCY =
            "UPDATE Room SET current_occupancy = current_occupancy + 1 WHERE room_id = ? AND current_occupancy < max_capacity";

    private static final String DECREASE_OCCUPANCY =
            "UPDATE Room SET current_occupancy = current_occupancy - 1 WHERE room_id = ? AND current_occupancy > 0";

    private static final String GET_ROOM_OCCUPANCY =
            "SELECT current_occupancy, max_capacity FROM Room WHERE room_id = ?";

    private static final String GET_CURRENT_ROOM =
            "SELECT room_id FROM Dormer WHERE dormer_id = ?";



    public boolean addDormer(Dormer dormer) {
        try (Connection connection = getConnection();
             PreparedStatement checkDormerStmt = connection.prepareStatement("SELECT COUNT(*) FROM Dormer WHERE email = ?");
             PreparedStatement checkRoomStmt = connection.prepareStatement(GET_ROOM_OCCUPANCY);
             PreparedStatement insertDormerStmt = connection.prepareStatement(INSERT_DORMER, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement updateRoomStmt = connection.prepareStatement(INCREASE_OCCUPANCY)) {

            connection.setAutoCommit(false);

            // Check if the dormer already exists
            checkDormerStmt.setString(1, dormer.getEmail());
            try (ResultSet rs = checkDormerStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Dormer already exists", ButtonType.OK);
                    alert.show();
                    return false;
                }
            }

            // Check if the room has space
            checkRoomStmt.setInt(1, dormer.getRoomId());
            try (ResultSet rs = checkRoomStmt.executeQuery()) {
                if (rs.next()) {
                    int currentOccupancy = rs.getInt("current_occupancy");
                    int maxCapacity = rs.getInt("max_capacity");

                    if (currentOccupancy >= maxCapacity) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Room full", ButtonType.OK);
                        alert.show();
                        return false;
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Room not found", ButtonType.OK);
                    alert.show();
                    return false;
                }
            }

            // Insert Dormer
            setDormerParameters(insertDormerStmt, dormer);
            if (insertDormerStmt.executeUpdate() > 0) {
                try (ResultSet rs = insertDormerStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        dormer.setDormerId(rs.getInt(1));
                    }
                }
            }

            // Increase Room Occupancy
            updateRoomStmt.setInt(1, dormer.getRoomId());
            updateRoomStmt.executeUpdate();

            connection.commit();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dormer Added!", ButtonType.OK);
            alert.show();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDormer(int dormerId) {
        try (Connection conn = getConnection();
             PreparedStatement getRoomStmt = conn.prepareStatement(GET_CURRENT_ROOM);
             PreparedStatement deleteDormerStmt = conn.prepareStatement(DELETE_DORMER);
             PreparedStatement decreaseOccupancyStmt = conn.prepareStatement(DECREASE_OCCUPANCY)) {

            conn.setAutoCommit(false);

            // Get the dormer's current room
            getRoomStmt.setInt(1, dormerId);
            int roomId = -1;
            try (ResultSet rs = getRoomStmt.executeQuery()) {
                if (rs.next()) {
                    roomId = rs.getInt("room_id");
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Dormer not found", ButtonType.OK);
                    alert.show();
                    return false;
                }
            }

            // Delete dormer
            deleteDormerStmt.setInt(1, dormerId);
            deleteDormerStmt.executeUpdate();

            // Decrease room occupancy
            decreaseOccupancyStmt.setInt(1, roomId);
            decreaseOccupancyStmt.executeUpdate();

            conn.commit();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dormer Deleted!", ButtonType.OK);
            alert.show();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateDormer(Dormer dormer) {
        try (Connection conn = getConnection();
             PreparedStatement checkRoomStmt = conn.prepareStatement(GET_ROOM_OCCUPANCY);
             PreparedStatement updateDormerStmt = conn.prepareStatement(UPDATE_DORMER);
             PreparedStatement decreaseOldRoomStmt = conn.prepareStatement(DECREASE_OCCUPANCY);
             PreparedStatement increaseNewRoomStmt = conn.prepareStatement(INCREASE_OCCUPANCY);
             PreparedStatement getOldRoomStmt = conn.prepareStatement(GET_CURRENT_ROOM)) {

            conn.setAutoCommit(false);

            // Get current room of the dormer
            getOldRoomStmt.setInt(1, dormer.getDormerId());
            int oldRoomId = -1;
            try (ResultSet rs = getOldRoomStmt.executeQuery()) {
                if (rs.next()) {
                    oldRoomId = rs.getInt("room_id");
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Dormer not found", ButtonType.OK);
                    alert.show();
                    return false;
                }
            }

            // If the room is changing, check if the new room has space
            if (oldRoomId != dormer.getRoomId()) {
                checkRoomStmt.setInt(1, dormer.getRoomId());
                try (ResultSet rs = checkRoomStmt.executeQuery()) {
                    if (rs.next()) {
                        int currentOccupancy = rs.getInt("current_occupancy");
                        int maxCapacity = rs.getInt("max_capacity");

                        if (currentOccupancy >= maxCapacity) {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "New room is full", ButtonType.OK);
                            alert.show();
                            return false;
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "New room not found", ButtonType.OK);
                        alert.show();
                        return false;
                    }
                }
            }

            // Update Dormer Details
            setDormerParameters(updateDormerStmt, dormer);
            updateDormerStmt.setInt(6, dormer.getDormerId());
            updateDormerStmt.executeUpdate();

            // Adjust room occupancy if the dormer changed rooms
            if (oldRoomId != dormer.getRoomId()) {
                decreaseOldRoomStmt.setInt(1, oldRoomId);
                decreaseOldRoomStmt.executeUpdate();

                increaseNewRoomStmt.setInt(1, dormer.getRoomId());
                increaseNewRoomStmt.executeUpdate();
            }

            conn.commit();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dormer Updated!", ButtonType.OK);
            alert.show();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    private void setDormerParameters(PreparedStatement pstmt, Dormer dormer)
            throws SQLException {
        pstmt.setString(1, dormer.getFirstName());
        pstmt.setString(2, dormer.getLastName());
        pstmt.setString(3, dormer.getNumber());
        pstmt.setString(4, dormer.getEmail());
        pstmt.setInt(5, dormer.getRoomId());
    }

    private Dormer mapResultSetToDormer(ResultSet rs) throws SQLException {
        return new Dormer(
                rs.getInt("dormer_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("contact_number"),
                rs.getString("email"),
                rs.getInt("room_id")
        );
    }

    public String getDormerCount(){
        int total = 0;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(COUNT_ROWS);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt("total");
                System.out.println("Total number of rows: " + total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "" + total;
    }

    public ObservableList<Dormer> getDormers(String filter){
        ObservableList<Dormer> dormers = FXCollections.observableArrayList();
        String query = filter == null || filter.isEmpty() ? GET_ALL_DORMERS : GET_FILTERED_DORMERS;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            if (filter != null && !filter.isEmpty()) {
                String searchPattern = "%" + filter + "%";
                pstmt.setString(1, searchPattern);
                pstmt.setString(2, searchPattern);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    dormers.add(mapResultSetToDormer(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dormers;
    }

}
