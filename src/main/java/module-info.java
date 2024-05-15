module com.example.momentumv1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.xml.crypto;
    requires java.desktop;
    requires org.apache.commons.codec;

    opens com.example.momentumv1 to javafx.fxml;
    exports com.example.momentumv1;
   // opens com.example.momentum to javafx.fxml;
}