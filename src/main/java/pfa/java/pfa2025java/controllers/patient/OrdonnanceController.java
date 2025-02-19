package pfa.java.pfa2025java.controllers.patient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.Medicament;
import pfa.java.pfa2025java.model.OrdonnanceDAO;
import pfa.java.pfa2025java.model.OrdonnanceDetails;

import java.util.ArrayList;
import java.util.List;


public class OrdonnanceController {

    public TableView<OrdonnanceDetails> ordonnancesTable;
    public TableColumn <OrdonnanceDetails,String> MedecinColOrd;
    public TableColumn <OrdonnanceDetails,String> dateColOrd;
    public TableView <Medicament> medicamentTable;
    public TableColumn<OrdonnanceDetails,String> mednameCol;
    public TableColumn<Medicament,Integer> quantiteCol;
    public TableColumn<Medicament,String> instruCol;


    private final ObservableList<OrdonnanceDetails> ordonnanceDetailsList = FXCollections.observableArrayList();
    private final ObservableList<Medicament> medicamentList = FXCollections.observableArrayList();


    public void initialize() {
        MedecinColOrd.setCellValueFactory(new PropertyValueFactory<>("medecinNom"));
        dateColOrd.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));

        mednameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        quantiteCol.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        instruCol.setCellValueFactory(new PropertyValueFactory<>("instructions"));

        // Associer les listes aux tables
        ordonnancesTable.setItems(ordonnanceDetailsList);
        medicamentTable.setItems(medicamentList);

        // Ajouter un listener pour charger les médicaments lors de la sélection d'une ordonnance
        ordonnancesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                medicamentList.setAll(newSelection.getMedicaments()); // Charger les médicaments
            }
        });

        // Charger les données initiales
        loadOrdonnances();

    }
    private void loadOrdonnances() {
        int patientId = UserSession.getId();  // Remplacez par la vraie source de l'ID patient
        List<OrdonnanceDetails> ordonnances = OrdonnanceDAO.getOrdonnanceDetailsByPatient(patientId);
        ordonnanceDetailsList.setAll(ordonnances); // Mise à jour de la TableView
    }
    public void consulterRdv(ActionEvent actionEvent) {
    }

    public void consulterPharmacies(ActionEvent actionEvent) {
    }

    public void demandeRendezVous(ActionEvent actionEvent) {
    }

    public void logout(ActionEvent actionEvent) {
    }

    public void gotoaccueil(ActionEvent actionEvent) {
    }
}
