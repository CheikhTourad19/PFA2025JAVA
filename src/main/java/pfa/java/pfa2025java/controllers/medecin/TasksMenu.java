package pfa.java.pfa2025java.controllers.medecin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class TasksMenu {
    @FXML private BorderPane mainLayout;
    @FXML private AnchorPane contentPane;

    public void initialize() {
        loadDefaultView();
    }

    private void loadDefaultView() {
        loadView("/pfa/java/pfa2025java/views/medecin/task/createtask-view.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane view = loader.load();

            contentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (IOException e) {
            System.err.println("Erreur de chargement: " + fxmlPath);
            e.printStackTrace();
        }
    }

    @FXML
    public void loadCreateTask() {
        loadView("/pfa/java/pfa2025java/views/medecin/task/createtask-view.fxml");
    }

    @FXML
    public void loadMyTasks() {
        loadView("/pfa/java/pfa2025java/views/medecin/task/mytask-view.fxml");
    }

    @FXML
    public void loadFollowedTasks() {
        loadView("/pfa/java/pfa2025java/views/medecin/task/followtask-view.fxml");
    }
}
