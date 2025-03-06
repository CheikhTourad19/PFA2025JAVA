package pfa.java.pfa2025java.controllers.patient;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;

public class SidebarController {

    public Text nomUtil;
    public VBox mainContent;

    public void initialize() {
        nomUtil.setText(UserSession.getNom() + " " + UserSession.getPrenom());
        NavigationUtil.navigateTo(mainContent, "profile", this);

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

    public void demandeRendezVous(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "demanderdv", this);
    }

    public void chat(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "chatbot", this);
    }

    public void consulterPharmacies(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "pharmacie", this);
    }

    public void consulterOrdonnances(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "ordonnance", this);
    }

    public void consulterProfil(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "profile", this);
    }

    public void consulterRDV(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "accueil", this);
    }
}
