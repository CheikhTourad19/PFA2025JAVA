package pfa.java.pfa2025java.controllers.medecin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.RendezVous;
import pfa.java.pfa2025java.dao.RendezVousDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;


public class RendezVousController {
    @FXML
    private TableView<RendezVous> tableViewConfirme;
    @FXML
    private TableView<RendezVous> tableViewDemandes;

    @FXML
    private TableColumn<RendezVous, Integer> colIdConfirme, colIdDemande;
    @FXML
    private TableColumn<RendezVous, String> colDateConfirme, colDateDemande;
    @FXML
    private TableColumn<RendezVous, String> colStatutConfirme, colStatutDemande;
    @FXML
    private TableColumn<RendezVous, Integer> colPatientConfirme, colPatientDemande;


    private int medecinId = UserSession.getId();
    public void initialize() {
        // Configurer les colonnes pour la table des rendez-vous confirmés
        colIdConfirme.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDateConfirme.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPatientConfirme.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colStatutConfirme.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Configurer les colonnes pour la table des demandes de rendez-vous
        colIdDemande.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDateDemande.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPatientDemande.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colStatutDemande.setCellValueFactory(new PropertyValueFactory<>("statut"));

        loadRendezVousData();
    }
    private void loadRendezVousData() {
        try {
            ObservableList<RendezVous> rendezVousConfirmes = FXCollections.observableArrayList(RendezVousDAO.getRendezVousConfirmes(medecinId));
            ObservableList<RendezVous> demandesRendezVous = FXCollections.observableArrayList(RendezVousDAO.getDemandesRendezVous(medecinId));
            tableViewConfirme.setItems(rendezVousConfirmes);
            tableViewDemandes.setItems(demandesRendezVous);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void accepterRendezVous(ActionEvent actionEvent) {
    }

    public void refuserRendezVous(ActionEvent actionEvent) {
    }

    public void annulerRendezVous(ActionEvent actionEvent) {
    }
}
