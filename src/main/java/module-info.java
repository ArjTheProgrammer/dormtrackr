module application.dormtrackr {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires com.azure.communication.email;
    requires java.desktop;

    opens application.dormtrackr to javafx.fxml;
    exports application.dormtrackr;
    exports application.dormtrackr.controller;
    opens application.dormtrackr.controller to javafx.fxml;
}