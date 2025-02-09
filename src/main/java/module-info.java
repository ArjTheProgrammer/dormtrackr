module application.dormtrackr {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens application.dormtrackr to javafx.fxml;
    exports application.dormtrackr;
}