package application.dormtrackr.model.dao;

import application.dormtrackr.model.Dormer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DormerDAO extends BaseDAO<Dormer> {

    private static final String GET_ALL_DORMERS = "SELECT * FROM Dormer";
    private static final String INSERT_DORMER = "INSERT INTO Dormer (first_name, last_name, contact_number, email, room_id) VALUES (?, ?, ?, ?, ?)";
    private static final String COUNT_ROWS = "SELECT COUNT(*) AS total FROM Dormer";
    private static final String UPDATE_DORMER = "UPDATE Dormer SET first_name=?, last_name=?, contact_number=?, email=?, room_id=? WHERE dormer_id=?";
    private static final String DELETE_DORMER = "DELETE FROM Dormer WHERE dormer_id=?";


    public boolean addDormer(Dormer dormer) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_DORMER,
                     Statement.RETURN_GENERATED_KEYS)) {

            setDormerParameters(pstmt, dormer);

            if (pstmt.executeUpdate() > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        dormer.setDormerId(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
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

    public ObservableList<Dormer> getDormers(){
        ObservableList<Dormer> dormers = FXCollections.observableArrayList();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_ALL_DORMERS);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                dormers.add(mapResultSetToDormer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dormers;
    }

}
