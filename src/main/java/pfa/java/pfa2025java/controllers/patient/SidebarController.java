package pfa.java.pfa2025java.controllers.patient;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;

public class SidebarController {

    public Text nomUtil;
    public VBox mainContent;
    public Button accueilButton;
    public Button ordonnanceButton;
    public Button rdvButton;
    public Button pharmaButton;
    public Button demandeButton;
    public Button chatButton;
    private Button activeButton;


    public void initialize() {
        nomUtil.setText(UserSession.getNom() + " " + UserSession.getPrenom());
        NavigationUtil.navigateTo(mainContent, "profile", this);
        setActiveButton(accueilButton);

    }

    private void setActiveButton(Button button) {
        // Réinitialiser les styles des boutons
        ordonnanceButton.setStyle(" -fx-cursor: hand; -fx-background-color: #34495E; -fx-text-fill: white; -fx-pref-width: 180; -fx-font-size: 14px; -fx-font-weight: bold;");
        chatButton.setStyle(" -fx-cursor: hand; -fx-background-color: #34495E; -fx-text-fill: white; -fx-pref-width: 180; -fx-font-size: 14px; -fx-font-weight: bold;");
        accueilButton.setStyle(" -fx-cursor: hand; -fx-background-color: #34495E; -fx-text-fill: white; -fx-pref-width: 180; -fx-font-size: 14px; -fx-font-weight: bold;");
        rdvButton.setStyle(" -fx-cursor: hand; -fx-background-color: #34495E; -fx-text-fill: white; -fx-pref-width: 180; -fx-font-size: 14px; -fx-font-weight: bold;");
        demandeButton.setStyle(" -fx-cursor: hand; -fx-background-color: #34495E; -fx-text-fill: white; -fx-pref-width: 180; -fx-font-size: 14px; -fx-font-weight: bold;");
        pharmaButton.setStyle(" -fx-cursor: hand; -fx-background-color: #34495E; -fx-text-fill: white; -fx-pref-width: 180; -fx-font-size: 14px; -fx-font-weight: bold;");

        // Appliquer un style au bouton actif
        button.setStyle(" -fx-cursor: hand; -fx-background-color: #3498db; -fx-text-fill: white; -fx-pref-width: 180; -fx-font-size: 14px; -fx-font-weight: bold;"); // Bleu clair
        activeButton = button;
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
        setActiveButton(demandeButton);
    }

    public void chat(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "chatbot", this);
        setActiveButton(chatButton);
    }

    public void consulterPharmacies(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "pharmacie", this);
        setActiveButton(pharmaButton);
    }

    public void consulterOrdonnances(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "ordonnance", this);
        setActiveButton(ordonnanceButton);
    }

    public void consulterProfil(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "profile", this);
        setActiveButton(accueilButton);

    }

    public void consulterRDV(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "accueil", this);
        setActiveButton(rdvButton);
    }
}
