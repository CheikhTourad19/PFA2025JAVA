package pfa.java.pfa2025java.controllers.medecin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.RendezVous;
import pfa.java.pfa2025java.dao.RendezVousDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;


public class RendezVousController {
    public TextField timeFieldRdv;
    public DatePicker datePickerRdv;
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
        RendezVous selectedRdv = tableViewDemandes.getSelectionModel().getSelectedItem();
        if (selectedRdv != null) {
            LocalDate date = datePickerRdv.getValue();
            String time = timeFieldRdv.getText();

            if (date == null || time.isEmpty()) {
                System.out.println("Veuillez sélectionner une date et une heure.");
                return;
            }
            String formattedDateTime = date + " " + time;
            RendezVous rendezVous = selectedRdv;
            rendezVous.setDate(formattedDateTime);
            rendezVous.setStatut("confirme");

            try {
                RendezVousDAO.updateRendezVous(rendezVous);
                loadRendezVousData(); // Refresh tables
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void refuserRendezVous(ActionEvent actionEvent) {
        RendezVous selectedRdv = tableViewDemandes.getSelectionModel().getSelectedItem();
        if (selectedRdv != null) {
            try {
                RendezVousDAO.updateRdvState(selectedRdv.getId(),"annule");
                loadRendezVousData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void annulerRendezVous(ActionEvent actionEvent) {
        RendezVous selectedRdv = tableViewConfirme.getSelectionModel().getSelectedItem();
        if (selectedRdv != null) {
            try {
                RendezVousDAO.deleteRendezVous(selectedRdv.getId());
                loadRendezVousData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
