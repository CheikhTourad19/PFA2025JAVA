package pfa.java.pfa2025java.controllers.pharmacie;

import javafx.event.ActionEvent;
import pfa.java.pfa2025java.SwtichScene;

public class StockController {
    public void gotoaccueil(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/pharmacie/accueil-view.fxml","Accueil",false);
    }
}
