package pfa.java.pfa2025java.controllers.patient;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pfa.java.pfa2025java.SwtichScene;
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
    public ProgressIndicator loading;


    public void initialize() {
        MedecinColOrd.setCellValueFactory(new PropertyValueFactory<>("medecinNom"));
        dateColOrd.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));

        mednameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        quantiteCol.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        instruCol.setCellValueFactory(new PropertyValueFactory<>("instruction"));

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
        loading.setVisible(true);

        Task<List<OrdonnanceDetails>> task = new Task<>() {
            @Override
            protected List<OrdonnanceDetails> call() {
                return OrdonnanceDAO.getOrdonnanceDetailsByPatient(patientId);
            }

            @Override
            protected void succeeded() {
                // Cacher le loader et mettre à jour l'UI avec les données
                Platform.runLater(() -> {
                    ordonnanceDetailsList.setAll(getValue());
                    loading.setVisible(false);
                });
            }

            @Override
            protected void failed() {
                // En cas d'erreur, cacher le loader
                Platform.runLater(() -> loading.setVisible(false));
            }
        };

        // Exécuter la tâche dans un nouveau thread
        new Thread(task).start();

    }
    public void consulterPharmacies(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/patient/accueil-view.fxml","Pharmacie",false);
    }

    public void demandeRendezVous(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/patient/demanderdv-view.fxml","demande",false);
    }

    public void logout(ActionEvent actionEvent) {
        UserSession.logout();
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/hello-view.fxml","Accueil",false);
    }

    public void gotoaccueil(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/patient/pharmacie-view.fxml","Accueil",false);
    }

    public void gotprofil(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/patient/profile-view.fxml","profile",false);
    }

}
