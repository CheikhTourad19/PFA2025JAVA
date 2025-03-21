package pfa.java.pfa2025java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class LinkController {

        @FXML
        private Hyperlink downloadLink;

        @FXML
        private void handleDownload() {
            try {
                // Use double backslashes (\\) to properly escape the path in Java
                File file = new File("C:\\Users\\khali\\OneDrive\\Desktop\\cyc 1\\SEM2\\medical App");

                if (!file.exists()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Le fichier n'existe pas !");
                    alert.showAndWait();
                    return;
                }

                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Impossible d'ouvrir le fichier.");
                alert.showAndWait();
            }
        }}
