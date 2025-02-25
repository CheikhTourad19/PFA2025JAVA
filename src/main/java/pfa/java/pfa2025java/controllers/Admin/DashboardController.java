package pfa.java.pfa2025java.controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import java.io.IOException;

import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;

public class DashboardController {
    @FXML
    private Label UserNameLabel;

    @FXML
    private VBox mainContent;  // This is where views will load dynamically



    public void initialize() {
        UserNameLabel.setText("Mr. " + UserSession.getPrenom() + " " + UserSession.getNom());
        loadView("/pfa/java/pfa2025java/views/Admin/Home.fxml");  // Load default view
    }

    // Method to load a view inside the main content area
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent newView = loader.load();
            mainContent.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gotodashboard(ActionEvent actionEvent) {
        loadView("/pfa/java/pfa2025java/views/Admin/Home.fxml");
    }

    public void gotousers(ActionEvent actionEvent) {
        loadView("/pfa/java/pfa2025java/views/Admin/Users.fxml");
    }

    public void gotorapports(ActionEvent actionEvent) {
        loadView("/pfa/java/pfa2025java/views/Admin/Rapports.fxml");
    }

    public void gotoparametre(ActionEvent actionEvent) {
        loadView("/pfa/java/pfa2025java/views/Admin/Parametres.fxml");
    }

    public void gotodeconnexion(ActionEvent actionEvent) {
        UserSession.logout();
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"/pfa/java/pfa2025java/views/hello-view.fxml","connexion",false);
    }

    public void chatbot(ActionEvent actionEvent) {
        loadView("/pfa/java/pfa2025java/views/chatbot.fxml");
    }

}
