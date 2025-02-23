package pfa.java.pfa2025java.controllers.medecin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import pfa.java.pfa2025java.model.RendezVous;
import pfa.java.pfa2025java.model.MedecinDAO;

public class MedecinController {


    @FXML
    private TableView<RendezVous> rdvTable; // La table affichant les rendez-vous

    // Méthode pour accepter un rendez-vous
    @FXML
    private void acceptRDV() {
        // Vérifie si un rendez-vous est sélectionné dans la table
        RendezVous selectedRdv = rdvTable.getSelectionModel().getSelectedItem();

        if (selectedRdv != null) {
            // Met à jour le statut du rendez-vous à "Accepté"
            //?????
            MedecinDAO.updateRDVStatus(selectedRdv.getId(), "Accepté");

            // Affiche une confirmation que le rendez-vous a été accepté
            Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
            confirmation.setTitle("Rendez-vous accepté");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Le rendez-vous a été accepté !");
            confirmation.showAndWait();

            // Recharge la liste des rendez-vous après modification
            loadPendingRDVs();
        } else {
            // Si aucun rendez-vous n'est sélectionné, afficher un message d'erreur
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText("Aucun rendez-vous sélectionné");
            errorAlert.setContentText("Veuillez sélectionner un rendez-vous avant d'accepter.");
            errorAlert.showAndWait();
        }
    }

    // Méthode fictive pour recharger la liste des rendez-vous (à implémenter)
    private void loadPendingRDVs() {
        // Logic to load pending rendez-vous into rdvTable
    }
}
