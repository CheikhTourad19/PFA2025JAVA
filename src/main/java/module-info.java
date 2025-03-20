module pfa.java.pfa2025java {
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jbcrypt;
    requires java.prefs;
    requires org.apache.pdfbox;
    requires javafaker;
    requires okhttp3;
    requires org.json;
    requires mysql.connector.java;
    requires java.mail;
    requires twilio;
    requires javafx.web;


    opens pfa.java.pfa2025java to javafx.fxml;
    opens pfa.java.pfa2025java.controllers.pharmacie to javafx.fxml;
    opens pfa.java.pfa2025java.controllers.patient to javafx.fxml;
    opens pfa.java.pfa2025java.controllers.Admin to javafx.fxml;
    opens pfa.java.pfa2025java.controllers.medecin to javafx.fxml;
    opens pfa.java.pfa2025java.controllers.medecin.util to javafx.fxml;


    exports pfa.java.pfa2025java;
    exports pfa.java.pfa2025java.controllers;
    exports pfa.java.pfa2025java.model;

    exports pfa.java.pfa2025java.controllers.pharmacie;
    exports pfa.java.pfa2025java.controllers.Admin;
    opens pfa.java.pfa2025java.controllers to javafx.fxml;
    exports pfa.java.pfa2025java.dao;
}