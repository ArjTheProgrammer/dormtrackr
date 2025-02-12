package application.dormtrackr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 560);

        stage.setTitle("DormTrackr");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
//        String url = "jdbc:sqlserver://property-database.database.windows.net:1433;"
//                + "database=PropertyManagementSystem;"
//                + "user=rjsilagan@property-database;"
//                + "password=Dormtrackr888;"
//                + "encrypt=true;"
//                + "trustServerCertificate=false;"
//                + "loginTimeout=30;";
    }
}