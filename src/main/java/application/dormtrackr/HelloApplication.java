package application.dormtrackr;

import application.dormtrackr.util.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 560);

        stage.setTitle("DormTrackr");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        // Close the HikariCP connection pool
        DatabaseConnection.closePool();
        System.out.println("Application stopped and connection pool closed.");
    }

    public static void main(String[] args) {
        launch();
//        HelloApplication.getDataSample();
    }

//    public static void getDataSample() throws SQLException {
//        String url = "jdbc:sqlserver://property-database.database.windows.net:1433;"
//                + "database=PropertyManagementSystem;"
//                + "user=rjsilagan@property-database;"
//                + "password=Dormtrackr888;"
//                + "encrypt=true;"
//                + "trustServerCertificate=false;"
//                + "loginTimeout=30;";
//        Connection connection = null;
//        String statement;
//        PreparedStatement preparedStatement = null;
//        ResultSet resultSet = null;
//        try {
//            System.out.println("connecting");
//
//            connection = DriverManager.getConnection(url);
//            statement = "SELECT last_name, first_name FROM Dormer ORDER BY last_name ASC";
//            preparedStatement = connection.prepareStatement(statement);
//            resultSet = preparedStatement.executeQuery();
//
//            System.out.println("connected");
//
//            while (resultSet.next()){
//                //get all the data
////                System.out.printf("%-20s %-20s %-20s %-20s %-20s%n",
////                        resultSet.getString(1),
////                        resultSet.getString(2),
////                        resultSet.getString(3),
////                        resultSet.getString(4),
////                        resultSet.getString(5));
//
//                //get the last name and first name
//                System.out.printf("%-20s %-20s%n",
//                        resultSet.getString(1),
//                        resultSet.getString(2));
//            }
//
//        } catch (SQLException e){
//            System.out.println("Error connecting to the database: " + e);
//        }
//        finally {
//            assert preparedStatement != null;
//            preparedStatement.close();
//            assert resultSet != null;
//            resultSet.close();
//            connection.close();
//            System.out.println("closed");
//        }
//    }
}