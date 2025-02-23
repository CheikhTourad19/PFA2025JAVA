package pfa.java.pfa2025java.controllers.patient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import pfa.java.pfa2025java.UserSession;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pfa.java.pfa2025java.model.RendezVous;
import pfa.java.pfa2025java.model.MedecinDAO;

import java.sql.SQLException;

public class MesRDVController {

    @FXML
    private TableView<RendezVous> rdvTable;
    @FXML
    private TableColumn<RendezVous, String> medecinNomCol;
    @FXML
    private TableColumn<RendezVous, String> statutCol;

    private ObservableList<RendezVous> rdvList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        medecinNomCol.setCellValueFactory(new PropertyValueFactory<>("medecinNom"));
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));

        try {
            loadRendezVous();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadRendezVous() throws SQLException {
        rdvList.clear();
        // Remplir la liste avec les rendez-vous du patient
        rdvList.addAll(MedecinDAO.getAcceptedRendezVous(UserSession.getId()));
        rdvTable.setItems(rdvList);
    }

    @FXML
    private void handleCancelRDV() throws SQLException {
        RendezVous selectedRDV = rdvTable.getSelectionModel().getSelectedItem();

        if (selectedRDV != null) {
            // Supprimer ou annuler un rendez-vous si nécessaire
            Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
            confirmation.setTitle("Rendez-vous annulé");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Votre rendez-vous a été annulé.");
            confirmation.showAndWait();

            // Rafraîchir la liste après annulation
            loadRendezVous();
        }
    }
    // Méthode pour accepter un rendez-vous
    @FXML
    private void acceptRDV() {
        RendezVous selectedRdv = rdvTable.getSelectionModel().getSelectedItem();

        if (selectedRdv != null) {
            MedecinDAO.updateRDVStatus(selectedRdv.getId(), "Accepté");

            Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
            confirmation.setTitle("Rendez-vous accepté");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Le rendez-vous a été accepté !");
            confirmation.showAndWait();

            loadRDVList(); // Méthode fictive pour actualiser la table
        }
    }

    // Méthode pour refuser un rendez-vous
    @FXML
    private void refuseRDV() {
        RendezVous selectedRdv = rdvTable.getSelectionModel().getSelectedItem();

        if (selectedRdv != null) {
            MedecinDAO.updateRDVStatus(selectedRdv.getId(), "Refusé");

            Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
            confirmation.setTitle("Rendez-vous refusé");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Le rendez-vous a été refusé !");
            confirmation.showAndWait();

            loadRDVList(); // Méthode fictive pour actualiser la table
        }
    }

    // Méthode fictive pour charger la liste des rendez-vous dans la table
    private void loadRDVList() {
        // Charger les rendez-vous depuis la base de données et les afficher dans la table
    }
}

