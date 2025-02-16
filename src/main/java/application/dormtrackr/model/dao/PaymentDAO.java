package application.dormtrackr.model.dao;

import application.dormtrackr.model.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class PaymentDAO extends BaseDAO<Payment> {
    private static final String GET_ALL_PAYMENTS = "SELECT * FROM Payment";
    private static final String GET_RATIO = "WITH PaidThisMonth AS (SELECT dormer_id FROM Payment WHERE YEAR(payment_date) = YEAR(GETDATE()) AND MONTH(payment_date) = MONTH(GETDATE())) SELECT COUNT(*) AS total_dormers, (SELECT COUNT(*) FROM PaidThisMonth) AS paid_dormers FROM Dormer";

    public String getPaidStudents(){
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_RATIO);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int totalDormers = rs.getInt("total_dormers");
                int paidDormers = rs.getInt("paid_dormers");
                return paidDormers + "/" + totalDormers;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "NULL";
    }

    public ObservableList<Payment> getPayments(){
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_ALL_PAYMENTS);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                payments.add(mapResultSetToPayments(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    private Payment mapResultSetToPayments(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getInt("payment_id"),
                rs.getInt("dormer_id"),
                rs.getDate("payment_date").toLocalDate());
    }
}
