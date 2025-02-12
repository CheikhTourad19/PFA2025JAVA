package pfa.java.pfa2025java.controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;

public class gestionUtilistaeurController {
    @FXML
    private Label tfUsername;
    public void initialize() {
        tfUsername.setText(UserSession.getPrenom() + " " + UserSession.getNom());
    }
    public void gotodashboard(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"/pfa/java/pfa2025java/views/Admin/hello-view.fxml","dashboard",false);
    }

    public void gotoGU(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"/pfa/java/pfa2025java/views/Admin/gestion-utilisateur.fxml","gestion utilistateur",false);
    }
}
