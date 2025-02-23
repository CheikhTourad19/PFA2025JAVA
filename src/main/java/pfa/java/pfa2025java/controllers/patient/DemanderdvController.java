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
import javafx.fxml.FXML;
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
