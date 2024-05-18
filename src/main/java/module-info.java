module com.example.momentumv {
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
    opens com.login to javafx.fxml;
    exports com.example.momentumv1;
    exports  com.database;
    exports com.config;
    exports  com.login;
    exports  com.registration;
    exports  com.notifcationPackage;
     opens com.registration to javafx.fxml;
   // opens com.example.momentum to javafx.fxml;
}