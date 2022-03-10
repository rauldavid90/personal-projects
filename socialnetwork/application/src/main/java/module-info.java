module com.example.socialnetworkgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires org.apache.pdfbox;

    opens com.example.domain to javafx.fxml, javafx.base;
    opens com.example.repository to javafx.fxml, javafx.base;
    opens com.example.service to javafx.fxml, javafx.base;
    opens com.example.socialnetworkgui to javafx.fxml, javafx.base;

    exports com.example.domain;
    exports com.example.repository;
    exports com.example.service;
    exports com.example.socialnetworkgui;
}