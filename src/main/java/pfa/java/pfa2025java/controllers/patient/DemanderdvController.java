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
import pfa.java.pfa2025java.model.*;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;


import javafx.scene.control.TextField;

import java.sql.SQLException;

public class DemanderdvController {

    public TableView MedecinTable;
    public TableColumn mednameCol;
    public TableColumn serviceCol;
    @FXML
    private Text Bienvenu;

    @FXML
    private TextField searchField;

    private final ObservableList<Medecin> medecinList = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        mednameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        serviceCol.setCellValueFactory(new PropertyValueFactory<>("service"));

        loadMedecins();
        loadSpecialties();

        // Activer la recherche dynamique
        setupSearchFilter();
    }


    private void loadMedecins() throws SQLException {
        medecinList.clear(); // Clear existing data (if any)

        // Fetch the list of doctors from the DAO
        medecinList.addAll(MedecinDAO.getAllMedecin());

        // Set the list in the TableView
        MedecinTable.setItems(medecinList);

        // Initialize columns
        //mednameCol.setCellValueFactory(new PropertyValueFactory<>("nom")); // Ensure "nom" matches Medecin class attribute
        //serviceCol.setCellValueFactory(new PropertyValueFactory<>("service")); // Ensure "specialite" matches Medecin class attribute
        //serviceCol.setCellValueFactory(cellData -> cellData.getValue().getService().asObject());
    }


    @FXML

    public void consulterProfil(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/profile-view.fxml", "Profil", false);
    }
    @FXML
    private ComboBox<String> specialtyComboBox;

    private void loadSpecialties() {
        specialtyComboBox.getItems().clear(); // Nettoyer le ComboBox avant de charger les nouvelles valeurs

        // Récupérer les spécialités depuis la base de données via MedecinDAO
        specialtyComboBox.getItems().addAll(MedecinDAO.getAllSpecialties());
    }


    private void setupSearchFilter() {
        FilteredList<Medecin> filteredData = new FilteredList<>(medecinList, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(medecin -> {
                // Si la barre de recherche est vide, afficher tout
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                // Convertir la recherche en minuscule pour éviter la casse
                String lowerCaseFilter = newValue.toLowerCase();

                // Vérifier si le nom ou le service du médecin contient la recherche
                return medecin.getNom().toLowerCase().contains(lowerCaseFilter) ||
                        medecin.getService().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Trier les résultats
        SortedList<Medecin> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(MedecinTable.comparatorProperty());

        // Appliquer les données filtrées à la TableView
        MedecinTable.setItems(sortedData);
    }

    @FXML
    private void handleMedecinSelection() {
        Medecin selectedMedecin = (Medecin) MedecinTable.getSelectionModel().getSelectedItem();

        if (selectedMedecin != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Prendre un rendez-vous");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous prendre un rendez-vous avec " + selectedMedecin.getNom() + " ?");

            // Ajouter le bouton OK
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                sendRDVRequest(selectedMedecin.getId());
            }
        }
    }
    private void sendRDVRequest(int medecinId) {
        int patientId = UserSession.getCurrentUser().getId(); // Récupérer l'ID du patient connecté
        MedecinDAO.sendRDVRequest(medecinId, patientId);

        Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
        confirmation.setTitle("Demande envoyée");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Votre demande de rendez-vous a été envoyée !");
        confirmation.showAndWait();
    }

    public void consulterOrdonnances(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/ordonnance-view.fxml", "Ordonnances", false);
    }

    public void demandeRendezVous(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/demanderdv-view.fxml", "Rendez-vous", false);
    }
    public void mesRDV(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/mesRDV-view.fxml", "Rendez-vous", false);
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
