package pfa.java.pfa2025java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class LinkController {

    @FXML
    private Hyperlink downloadLink;

    @FXML
    private void handleDownload() {
        try {
            // URL of the file to download
            String fileURL = "https://we.tl/t-w48EgLI1Z8";

            // Attempt to open the URL in the default web browser
            URI uri = new URI(fileURL);

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(uri);
            } else {
                showErrorAlert("Impossible d'ouvrir le lien dans le navigateur.");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            showErrorAlert("L'URL est mal formatée.");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Impossible d'ouvrir le fichier.");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
