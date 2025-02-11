package pfa.java.pfa2025java.controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class HelloViewConroller {
    @FXML
    private StackPane contentArea;

    @FXML
    private Label lblUsername;

    // Méthodes pour changer le contenu
    @FXML
    private void showDashboard() {
        loadContent("dashboard-view.fxml");
    }

    @FXML
    private void showUsers() {
        loadContent("users-view.fxml");
    }

    private void loadContent(String fxmlFile) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Initialisation
    public void initialize() {
        lblUsername.setText("Dr. " + System.getProperty("user.name")); // Exemple dynamique
    }

    public void showAppointments(ActionEvent actionEvent) {
    }

    public void showMedicalRecords(ActionEvent actionEvent) {
    }

    public void showSettings(ActionEvent actionEvent) {
    }
}
