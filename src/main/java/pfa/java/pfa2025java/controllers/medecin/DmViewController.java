package pfa.java.pfa2025java.controllers.medecin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pfa.java.pfa2025java.dao.DossierMedicaleDAO;
import pfa.java.pfa2025java.dao.TraitementRDVDAO;
import pfa.java.pfa2025java.model.DossierMedical;
import pfa.java.pfa2025java.model.TraitementRDV;
import java.util.List;
import java.util.Optional;

public class DmViewController {
    @FXML private TextField txtPatientId, txtAllergies, txtAntecedentsMedicaux, txtAntecedentsFamiliaux;
    @FXML private ComboBox<String> cmbGroupeSanguin;
    @FXML private TableView<TraitementRDV> tableTraitements;
    @FXML private TableColumn<TraitementRDV, Integer> colId;
    @FXML private TableColumn<TraitementRDV, String> colTraitement, colObservations;

    private final DossierMedicaleDAO dossierMedicalDAO = new DossierMedicaleDAO();
    private final TraitementRDVDAO traitementRDVDAO = new TraitementRDVDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTraitement.setCellValueFactory(new PropertyValueFactory<>("traitement"));
        colObservations.setCellValueFactory(new PropertyValueFactory<>("observations"));
    }

    @FXML
    public void ajouterDossier() {
        try {
            int patientId = Integer.parseInt(txtPatientId.getText());
            String groupeSanguin = cmbGroupeSanguin.getValue();
            String allergies = txtAllergies.getText();
            String antecedentsMedicaux = txtAntecedentsMedicaux.getText();
            String antecedentsFamiliaux = txtAntecedentsFamiliaux.getText();

            DossierMedical dossier = new DossierMedical(0, patientId, groupeSanguin, allergies, antecedentsMedicaux, antecedentsFamiliaux, null, null);
            dossierMedicalDAO.addDossierMedical(dossier);
            showAlert("Succès", "Dossier médical ajouté avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un ID patient valide.");
        }
    }

    @FXML
    public void modifierDossier() {
        try {
            int patientId = Integer.parseInt(txtPatientId.getText());
            DossierMedical dossier = dossierMedicalDAO.getDossierByPatientId(patientId);
            if (dossier != null) {
                dossier.setGroupeSanguin(cmbGroupeSanguin.getValue());
                dossier.setAllergies(txtAllergies.getText());
                dossier.setAntecedentsMedicaux(txtAntecedentsMedicaux.getText());
                dossier.setAntecedentsFamiliaux(txtAntecedentsFamiliaux.getText());
                dossierMedicalDAO.updateDossierMedical(dossier);
                showAlert("Succès", "Dossier médical mis à jour !");
            } else {
                showAlert("Erreur", "Dossier non trouvé.");
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un ID patient valide.");
        }
    }

    @FXML
    public void supprimerDossier() {
        try {
            int patientId = Integer.parseInt(txtPatientId.getText());
            DossierMedical dossier = dossierMedicalDAO.getDossierByPatientId(patientId);
            if (dossier != null) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer ce dossier ?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = confirmation.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.YES) {
                    dossierMedicalDAO.deleteDossierMedical(dossier.getId());
                    showAlert("Succès", "Dossier supprimé !");
                }
            } else {
                showAlert("Erreur", "Dossier non trouvé.");
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un ID patient valide.");
        }
    }

    @FXML
    public void rechercherDossier() {
        try {
            int patientId = Integer.parseInt(txtPatientId.getText());
            DossierMedical dossier = dossierMedicalDAO.getDossierByPatientId(patientId);
            if (dossier != null) {
                cmbGroupeSanguin.setValue(dossier.getGroupeSanguin());
                txtAllergies.setText(dossier.getAllergies());
                txtAntecedentsMedicaux.setText(dossier.getAntecedentsMedicaux());
                txtAntecedentsFamiliaux.setText(dossier.getAntecedentsFamiliaux());

                // Charger les traitements associés
                List<TraitementRDV> traitements = dossier.getTraitementsRDV();
                ObservableList<TraitementRDV> data = FXCollections.observableArrayList(traitements);
                tableTraitements.setItems(data);
            } else {
                showAlert("Erreur", "Aucun dossier trouvé pour ce patient.");
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un ID patient valide.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
