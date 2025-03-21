package pfa.java.pfa2025java.controllers.infirmier;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class SidebarController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private void loadTasksView() {
        loadView("/pfa/java/pfa2025java/views/tasks-view.fxml");
    }

    @FXML
    private void loadNotesView() {
        loadView("/pfa/java/pfa2025java/views/note-view.fxml");
    }

    @FXML
    private void loadAppointmentsView() {
        // Charger la vue des rendez-vous (à créer)
        // loadView("/chemin/vers/rdv-view.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la vue: " + fxmlPath);
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Charger la vue des tâches par défaut au démarrage
        loadTasksView();
    }
}