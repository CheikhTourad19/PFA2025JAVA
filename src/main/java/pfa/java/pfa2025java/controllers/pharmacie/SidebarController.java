package pfa.java.pfa2025java.controllers.pharmacie;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;

public class SidebarController {
    public VBox mainContent;
    public Text name;
    public Button ordonnanceButton;
    public Button stockButton;
    public Button accueilButton;
    private Button activeButton;

    public void initialize() {
        NavigationUtil.navigateTo(mainContent, "accueil", this);
        name.setText(UserSession.getNom() + " " + UserSession.getPrenom());
        setActiveButton(accueilButton);

    }

    private void setActiveButton(Button button) {
        // Réinitialiser les styles des boutons
        ordonnanceButton.setStyle(" -fx-cursor: hand; -fx-background-color: #34495E; -fx-text-fill: white; -fx-pref-width: 180; -fx-font-size: 14px; -fx-font-weight: bold;");
        stockButton.setStyle(" -fx-cursor: hand; -fx-background-color: #34495E; -fx-text-fill: white; -fx-pref-width: 180; -fx-font-size: 14px; -fx-font-weight: bold;");
        accueilButton.setStyle(" -fx-cursor: hand; -fx-background-color: #34495E; -fx-text-fill: white; -fx-pref-width: 180; -fx-font-size: 14px; -fx-font-weight: bold;");

        // Appliquer un style au bouton actif
        button.setStyle(" -fx-cursor: hand; -fx-background-color: #3498db; -fx-text-fill: white; -fx-pref-width: 180; -fx-font-size: 14px; -fx-font-weight: bold;"); // Bleu clair
        activeButton = button;
    }

    public void gotoordonnance(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "ordonnance", this);
        setActiveButton(ordonnanceButton);
    }

    public void gotostock(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "stock", this);
        setActiveButton(stockButton);
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

    public void gotoaccueil(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "accueil", this);
        setActiveButton(accueilButton);
    }
}
