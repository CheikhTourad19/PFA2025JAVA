package pfa.java.pfa2025java.controllers.pharmacie;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.Medicament;
import pfa.java.pfa2025java.model.MedicamentDAO;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;


import java.util.List;

public class StockController {
    public Button accueilButton;
    @FXML
    private TableColumn<Medicament, String> nomColumn;
    @FXML
    private TableColumn<Medicament, Integer> prixColumn;
    @FXML
    private TableColumn<Medicament, String> descriptionColumn;
    @FXML
    private TableColumn<Medicament, Integer> stock;
    public Button addstockButton;
    public TextField stockFiled;
    public TextField nomField;
    public TextField prixField;
    public Button addmedicamentButton;
    public TextArea descriptionFiled;
    public TableView listeMedicamentStock;
    private ObservableList<Medicament> medicamentList = FXCollections.observableArrayList();
    @FXML
    private TextField searchField; // Add this for the search field

    private FilteredList<Medicament> filteredData;


    @FXML
    public void initialize() {
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        prixColumn.setCellValueFactory(cellData -> cellData.getValue().prixProperty().asObject());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        stock.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());

        loadMedicaments();
        // Initialize FilteredList with the full list
        filteredData = new FilteredList<>(medicamentList, p -> true);

        // Add a listener to searchField
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(medicament -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true; // Show all if search is empty
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return medicament.getNom().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Wrap FilteredList in a SortedList and bind it to TableView
        SortedList<Medicament> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(listeMedicamentStock.comparatorProperty());
        listeMedicamentStock.setItems(sortedData);
    }

    private void loadMedicaments() {
        List<Medicament> medicaments = MedicamentDAO.getMedicamentsByPharmacie();

        medicamentList.setAll(medicaments);
    }


    public void addstock(ActionEvent actionEvent) {
        Medicament selectedMedicament = (Medicament) listeMedicamentStock.getSelectionModel().getSelectedItem();
        if (selectedMedicament == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un médicament.");
            return;
        }

        int increment;
        try {
            increment = Integer.parseInt(stockFiled.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un nombre valide.");
            return;
        }

        boolean success = MedicamentDAO.ajouterStock(selectedMedicament.getId(), increment);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Stock mis à jour avec succès.");
            loadMedicaments();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour du stock.");
        }
    }

    public void addmecidament(ActionEvent actionEvent) {
        String nom = nomField.getText();
        String description = descriptionFiled.getText();
        String prix = prixField.getText();

        if (nom.isEmpty() || description.isEmpty() || prix.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        boolean success = MedicamentDAO.addMedicamentWithStock(nom, description, prix);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Médicament ajouté avec succès.");
            loadMedicaments();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout du médicament.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String erreur, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle("erreur");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    public void gotoaccueil(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/pharmacie/accueil-view.fxml", "Accueil", false);
    }

    public void gotoordonnance(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/pharmacie/ordonnance-view.fxml", "Ordonnance", false);
    }

    public void logout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Etes vous sur de vouloir vous deconnecter ?");
        alert.setHeaderText(null);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            SwtichScene swtichScene = new SwtichScene();
            swtichScene.loadScene(actionEvent, "views/hello-view.fxml", "Login", false);
            UserSession.logout();
        }

    }

}
