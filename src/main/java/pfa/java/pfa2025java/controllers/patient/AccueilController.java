package pfa.java.pfa2025java.controllers.patient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;

public class AccueilController {

    @FXML
    private Text Bienvenu;

    @FXML
    private Button accueilButton;

    @FXML
    private ComboBox<?> cityComboBox;

    @FXML
    private ComboBox<?> countryComboBox;

    @FXML
    private ComboBox<?> genderComboBox;

    @FXML
    private ProgressIndicator loading;

    @FXML
    private TableView<?> medicamentTable;

    @FXML
    private TableColumn<?, ?> mednameCol;

    @FXML
    private TableColumn<?, ?> quantiteCol;

    @FXML
    private Button searchButton;

    @FXML
    private Button searchDoctorsButton;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<?> specialtyComboBox;

    @FXML

    public void consulterProfil(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/profile-view.fxml", "Profil", false);
    }
    public void mesRDV(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/mesRDV-view.fxml", "Profil", false);
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
