module com.example.momentumv {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;// Added for HTMLEditor
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.xml.crypto;
    requires java.desktop;
    requires org.apache.commons.codec;
    requires jdk.javadoc;
    exports de.jensd.fx.glyphs.fontawesome;
    exports com.task;
    exports com.example.momentumv1;
    exports com.database;
    exports com.config;
    exports com.login;
    exports com.project;
    exports com.registration;
    exports com.visualisation;
    exports com.person;
    exports com.personeControllers;

    opens com.example.momentumv1 to javafx.fxml;
    opens com.login to javafx.fxml;
    opens com.project to javafx.fxml, javafx.base;
    opens com.person to javafx.fxml;
    opens com.registration to javafx.fxml;
    opens com.task to javafx.fxml;
    opens com.personeControllers to javafx.fxml;
}
