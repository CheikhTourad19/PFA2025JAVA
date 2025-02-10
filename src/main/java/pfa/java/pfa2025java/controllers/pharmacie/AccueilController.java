package pfa.java.pfa2025java.controllers.pharmacie;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import pfa.java.pfa2025java.SwtichScene;

public class AccueilController {

    public Button stockButton;
    public Button ordonnanceButton;

    public void gotostock(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/pharmacie/stock-view.fxml","Stock",false);
    }

    public void gotoordonnance(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/pharmacie/ordonnance-view.fxml","Ordonnance",false);
    }
}
