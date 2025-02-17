package application.dormtrackr.model.dao;

import application.dormtrackr.model.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class PaymentDAO extends BaseDAO<Payment> {
    private static final String GET_PAID_DORMERS_BY_DATE = "SELECT \n" +
            "    p.payment_id, \n" +
            "    p.dormer_id, \n" +
            "    CONCAT(d.first_name, ' ', d.last_name) AS dormer_name, \n" +
            "    p.payment_date \n" +
            "FROM Payment p \n" +
            "JOIN Dormer d ON p.dormer_id = d.dormer_id \n" +
            "WHERE YEAR(p.payment_date) = ? \n" +
            "AND MONTH(p.payment_date) = ?;";

    private static final String GET_UNPAID_DORMERS_BY_DATE = "SELECT \n" +
            "    NULL AS payment_id, \n" +
            "    d.dormer_id, \n" +
            "    CONCAT(d.first_name, ' ', d.last_name) AS dormer_name, \n" +
            "    NULL AS payment_date \n" +
            "FROM Dormer d \n" +
            "LEFT JOIN Payment p \n" +
            "ON d.dormer_id = p.dormer_id \n" +
            "AND YEAR(p.payment_date) = ? \n" +
            "AND MONTH(p.payment_date) = ? \n" +
            "WHERE p.payment_id IS NULL;";

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

    public ObservableList<Payment> getPayments(String filter, int month, int year){
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        String query;

        if ("Unpaid".equalsIgnoreCase(filter)) {
            query = GET_UNPAID_DORMERS_BY_DATE;
        }
        else {
            query = GET_PAID_DORMERS_BY_DATE;
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set query parameters for month and year
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapResultSetToPayments(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    private Payment mapResultSetToPayments(ResultSet rs) throws SQLException {
        int paymentId = rs.getObject("payment_id") != null ? rs.getInt("payment_id") : 0;
        int dormerId = rs.getInt("dormer_id");
        String dormerName = rs.getString("dormer_name");
        // Handle NULL payment_date properly
        Date paymentDateSql = rs.getDate("payment_date");
        LocalDate paymentDate = (paymentDateSql != null) ? paymentDateSql.toLocalDate() : null;

        return new Payment(paymentId, dormerId, dormerName, paymentDate);
    }
}
