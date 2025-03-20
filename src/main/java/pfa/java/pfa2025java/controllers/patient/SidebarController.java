package pfa.java.pfa2025java.controllers.patient;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.dao.MessageDAO;

import java.sql.SQLException;

public class SidebarController {

    public Text nomUtil;
    public VBox mainContent;
    public Button accueilButton;
    public Button ordonnanceButton;
    public Button rdvButton;
    public Button pharmaButton;
    public Button demandeButton;
    public Button chatButton;
    public Button messagerie;
    private Button activeButton;
    @FXML
    private Label countMsg;


    public void initialize() {
        nomUtil.setText(UserSession.getNom() + " " + UserSession.getPrenom());
        NavigationUtil.navigateTo(mainContent, "profile", this);
        setActiveButton(accueilButton);

    }

    private void setActiveButton(Button button) {
        // Reset all buttons to default style
        ordonnanceButton.getStyleClass().remove("active-button");
        chatButton.getStyleClass().remove("active-button");
        accueilButton.getStyleClass().remove("active-button");
        rdvButton.getStyleClass().remove("active-button");
        demandeButton.getStyleClass().remove("active-button");
        pharmaButton.getStyleClass().remove("active-button");

        // Apply active style to the selected button
        button.getStyleClass().add("active-button");
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
    } public void goToMsg(ActionEvent actionEvent) {
        NavigationUtil.navigateTo(mainContent, "message", this);
        setActiveButton(messagerie);
    }


}
