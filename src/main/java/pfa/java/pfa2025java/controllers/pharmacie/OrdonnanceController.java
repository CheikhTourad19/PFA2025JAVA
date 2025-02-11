package pfa.java.pfa2025java.controllers.pharmacie;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import pfa.java.pfa2025java.SwtichScene;

public class OrdonnanceController {

    public Button stockButton;
    public Button accueilButton;


    public void gotostock(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/pharmacie/stock-view.fxml", "Stock", false);
    }

    public void gotoaccueil(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/pharmacie/accueil-view.fxml","Accueil",false);
    }

    public void logout(ActionEvent actionEvent) {
    }
}
