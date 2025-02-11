package pfa.java.pfa2025java.controllers.pharmacie;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import pfa.java.pfa2025java.SwtichScene;

public class StockController {
    public Button accueilButton;

    public void gotoaccueil(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/pharmacie/accueil-view.fxml","Accueil",false);
    }

    public void gotoordonnance(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/pharmacie/ordonnance-view.fxml", "Ordonnance", false);
    }

    public void logout(ActionEvent actionEvent) {
    }
}
