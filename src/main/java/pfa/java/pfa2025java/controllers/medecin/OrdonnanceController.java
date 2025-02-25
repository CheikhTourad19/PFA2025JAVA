package pfa.java.pfa2025java.controllers.medecin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.Medicament;
import pfa.java.pfa2025java.model.MedicamentDAO;
import pfa.java.pfa2025java.model.OrdonnanceDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdonnanceController {

    @FXML
    private TextField patientTextField, quantiteTextField, instructionTextField;

    @FXML
    private TableView<Medicament> medicamentTable;
    @FXML
    private TableColumn<Medicament, String> nomMedicamentColumn;

    @FXML
    private TableView<Medicament> selectedMedicamentTable;
    @FXML
    private TableColumn<Medicament, String> selectedMedicamentColumn;
    @FXML
    private TableColumn<Medicament, String> instructionColumn;
    @FXML
    private TableColumn<Medicament, Integer> quantiteColumn;

    @FXML
    private Button ajouterButton, confirmerButton;

    private ObservableList<Medicament> medicamentList = FXCollections.observableArrayList();
    private ObservableList<Medicament> selectedMedicaments = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Setup columns
        nomMedicamentColumn.setCellValueFactory(data -> data.getValue().nomProperty());
        selectedMedicamentColumn.setCellValueFactory(data -> data.getValue().nomProperty());
        quantiteColumn.setCellValueFactory(data -> data.getValue().quantiteProperty().asObject());
        instructionColumn.setCellValueFactory(data -> data.getValue().instructionProperty());

        // Load medicaments from the database
        loadMedicaments();
    }

    private void loadMedicaments() {
        try {
            medicamentList.setAll(MedicamentDAO.getAllMedicaments());
            medicamentTable.setItems(medicamentList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les médicaments.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void ajouterMedicament() {
        Medicament selectedMedicament = medicamentTable.getSelectionModel().getSelectedItem();
        if (selectedMedicament == null) {
            showAlert("Erreur", "Veuillez sélectionner un médicament.", Alert.AlertType.WARNING);
            return;
        }

        String quantiteText = quantiteTextField.getText();
        String instructionText = instructionTextField.getText();

        if (quantiteText.isEmpty() || instructionText.isEmpty()) {
            showAlert("Erreur", "Veuillez saisir la quantité et l'instruction.", Alert.AlertType.WARNING);
            return;
        }

        try {
            int quantite = Integer.parseInt(quantiteText);
            Medicament m = selectedMedicament;
            m.setInstruction(instructionText);
            m.setQuantite(quantite);
            selectedMedicaments.add(m);
            selectedMedicamentTable.setItems(selectedMedicaments);
            quantiteTextField.clear();
            instructionTextField.clear();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "La quantité doit être un nombre.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void confirmerOrdonnance() {
        String patientName = patientTextField.getText();
        if (patientName.isEmpty()) {
            showAlert("Erreur", "Veuillez saisir le nom du patient.", Alert.AlertType.WARNING);
            return;
        }

        if (selectedMedicaments.isEmpty()) {
            showAlert("Erreur", "Aucun médicament sélectionné.", Alert.AlertType.WARNING);
            return;
        }

        try {
            int patientId = Integer.parseInt(patientTextField.getText());


            boolean success = OrdonnanceDAO.creerOrdonnance(
                    UserSession.getId(), // ID du médecin (à adapter dynamiquement)
                    patientId, // ID du patient (à adapter dynamiquement)
                    LocalDate.now().toString(), // Date de création automatique au format "YYYY-MM-DD"
                    selectedMedicaments
            );

            if (success) {
                showAlert("Succès", "Ordonnance créée avec succès.", Alert.AlertType.INFORMATION);
                selectedMedicaments.clear();
                patientTextField.clear();
            } else {
                showAlert("Erreur", "Échec de la création de l'ordonnance.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la création de l'ordonnance.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
