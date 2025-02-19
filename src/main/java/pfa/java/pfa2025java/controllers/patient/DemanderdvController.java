package pfa.java.pfa2025java.controllers.patient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.RendezVous;
import java.util.Date;

public class DemanderdvController {
    @FXML
    private Text Bienvenu;

    @FXML
    private TableView<RendezVous> medicamentTable;

    @FXML
    private TableColumn<RendezVous, String> mednameCol;

    @FXML
    private TableColumn<RendezVous, String> quantiteCol;

    private ObservableList<RendezVous> rendezVousList = FXCollections.observableArrayList();

    public void initialize() {
        Bienvenu.setText(UserSession.getPrenom() + " " + UserSession.getNom());

        // Initialisation des colonnes de la table
        mednameCol.setCellValueFactory(new PropertyValueFactory<>("medecinNom"));
        quantiteCol.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Chargement des rendez-vous
        chargerRendezVous();
    }

    private void chargerRendezVous() {
        // Simulation des rendez-vous (remplace avec une récupération depuis la base de données)
        rendezVousList.add(new RendezVous(1, 101, "Dr. Mohamed", 202, "Ali Ben Jannet", new Date(), "Confirmé"));
        rendezVousList.add(new RendezVous(2, 102, "Dr. Yassine", 202, "Ali Ben Jannet", new Date(), "En attente"));

        // Mise à jour de la table
        medicamentTable.setItems(rendezVousList);
    }


    @FXML

    public void consulterProfil(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/profile-view.fxml", "Profil", false);
    }

    public void consulterOrdonnances(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/ordonnance-view.fxml", "Ordonnances", false);
    }

    public void demandeRendezVous(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/demanderdv-view.fxml", "Rendez-vous", false);
    }

    public void consulterPharmacies(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/pharmacie-view.fxml", "Pharmacies", false);
    }
    public void logout(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/hello-view.fxml", "Login", false);
        UserSession.logout();
    }
}
