package application.dormtrackr.model.dao;

import application.dormtrackr.model.Dormer;
import com.azure.communication.email.*;
import com.azure.communication.email.models.*;
import com.azure.core.util.polling.PollResponse;
import com.azure.core.util.polling.SyncPoller;
import application.dormtrackr.model.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import javax.print.DocFlavor;
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

    String ADD_PAYMENT = "INSERT INTO Payment (dormer_id, payment_date) VALUES (?, ?)";

    String DORMER = "SELECT CONCAT(first_name, ' ', last_name) AS dormer_name, email FROM Dormer WHERE dormer_id = ?";

    String DELETE_PAYMENT = "DELETE FROM Payment WHERE payment_id = ?";

    String UPDATE_PAYMENT = "UPDATE Payment SET dormer_id = ?, payment_date = ? WHERE payment_id = ?";

    String CHECK_EXISTING_PAYMENT = "SELECT COUNT(*) FROM Payment WHERE dormer_id = ? AND YEAR(payment_date) = ? AND MONTH(payment_date) = ?";

    public boolean addPayment(int dormerId, LocalDate paymentDate) {
        try (Connection conn = getConnection()) {
            // First, check if a payment already exists for this dormer in the same month
            try (PreparedStatement checkStmt = conn.prepareStatement(CHECK_EXISTING_PAYMENT)) {
                checkStmt.setInt(1, dormerId);
                checkStmt.setInt(2, paymentDate.getYear());
                checkStmt.setInt(3, paymentDate.getMonthValue());

                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Dormer already paid this month!");
                    alert.show();
                    return false;
                }
            }

            // If no payment exists, proceed with adding the new payment
            try (PreparedStatement pstmt = conn.prepareStatement(ADD_PAYMENT)) {
                pstmt.setInt(1, dormerId);
                pstmt.setDate(2, Date.valueOf(paymentDate));

                int affectedRows = pstmt.executeUpdate();
                try (PreparedStatement sendPstmt = conn.prepareStatement(DORMER)){
                    sendPstmt.setInt(1, dormerId);
                    ResultSet rs = sendPstmt.executeQuery();
                    if (rs.next()) {
                        sendEmail(rs.getString("dormer_name"), rs.getString("email"), paymentDate);
                    }
                }
                return affectedRows > 0; // Returns true if insertion was successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePayment(int paymentId, int newDormerId, LocalDate newPaymentDate) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_PAYMENT)) {

            pstmt.setInt(1, newDormerId);
            pstmt.setDate(2, Date.valueOf(newPaymentDate));
            pstmt.setInt(3, paymentId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Returns true if the update was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean deletePayment(int paymentId) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_PAYMENT)) {

            pstmt.setInt(1, paymentId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Returns true if deletion was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


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

    private void sendEmail(String name, String email, LocalDate date){
        String connectionString = "endpoint=" + System.getenv("endpoint") + ";accesskey=" + System.getenv("accesskey");
        EmailClient emailClient = new EmailClientBuilder().connectionString(connectionString).buildClient();
        EmailAddress toAddress = new EmailAddress(email);

        String subject = "Payment Confirmation for " + date.getMonth() + " " + date.getYear();
        String bodyPlainText = "Dear " + name + ",\n\nThank you for your payment for the month of " + date.getMonth() + ".\n\nBest regards,\nDormtrackr";
        String bodyHtml = """
    <html>
        <body>
            <h1>Payment Confirmation</h1>
            <p>Dear %s,</p>
            <p>Thank you for your payment for the month of %s.</p>
            <p>Best regards,<br>Dormtrckr</p>
        </body>
    </html>
    """.formatted(name, date.getMonth());



        EmailMessage emailMessage = new EmailMessage()
                .setSenderAddress("DoNotReply@79e91150-75ae-4844-8d83-52a90063921b.azurecomm.net")
                .setToRecipients(toAddress)
                .setSubject(subject)
                .setBodyPlainText(bodyPlainText)
                .setBodyHtml(bodyHtml);

        SyncPoller<EmailSendResult, EmailSendResult> poller = emailClient.beginSend(emailMessage, null);
        PollResponse<EmailSendResult> result = poller.waitForCompletion();
        System.out.println("email sent!");
    }
}
