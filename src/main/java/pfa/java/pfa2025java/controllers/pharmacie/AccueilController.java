package pfa.java.pfa2025java.controllers.pharmacie;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.MedicamentDAO;

public class AccueilController {

    public Button stockButton;
    public Button ordonnanceButton;
    public Text name;

    public void initialize() {
        name.setText(UserSession.getPrenom() + " " + UserSession.getNom());
//        StockController.checkLowStock(MedicamentDAO.getMedicamentsByPharmacie());
    }

    public void gotostock(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/pharmacie/stock-view.fxml","Stock",false);
    }

    public void gotoordonnance(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/pharmacie/ordonnance-view.fxml","Ordonnance",false);
    }

    public void logout(ActionEvent actionEvent) {


    }
}
