package pfa.java.pfa2025java.controllers.pharmacie;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;

public class SidebarController {
    public VBox mainContent;
    public Text name;

    public void initialize() {
        NavigationUtil.navigateTo(mainContent, "accueil", this);
        name.setText(UserSession.getNom() + " " + UserSession.getPrenom());

    }

    public void gotoordonnance(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "ordonnance", this);
    }

    public void gotostock(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "stock", this);
    }

    public void logout(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Voulez vous vous deconnecter ?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            swtichScene.loadScene(actionEvent, "views/hello-view.fxml", "login", false);
            UserSession.logout();

        }
    }
}
