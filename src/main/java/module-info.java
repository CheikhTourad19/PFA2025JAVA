module pfa.java.pfa2025java {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;
    requires jbcrypt; // <-- Nécessaire pour SwingFXUtils
    opens pfa.java.pfa2025java to javafx.fxml;
    opens pfa.java.pfa2025java.controllers.pharmacie to javafx.fxml;
    exports pfa.java.pfa2025java;
    exports pfa.java.pfa2025java.controllers;
    exports pfa.java.pfa2025java.controllers.pharmacie;

    opens pfa.java.pfa2025java.controllers to javafx.fxml;
}