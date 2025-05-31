module org.example.testproj1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires org.locationtech.jts;
    requires javafx.media;
    requires java.desktop;
    exports mains to javafx.graphics;

    opens org.example.testproj1 to javafx.fxml;
}