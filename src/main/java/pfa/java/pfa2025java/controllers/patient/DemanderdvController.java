package pfa.java.pfa2025java.controllers.patient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import pfa.java.pfa2025java.EmaliSender;
import pfa.java.pfa2025java.SmsSender;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.controllers.ResetPasswordController;
import pfa.java.pfa2025java.dao.MedecinDAO;
import pfa.java.pfa2025java.model.*;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;


import java.sql.SQLException;
import java.util.Optional;

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
        loadSpecialties();

        // Activer la recherche dynamique
        setupSearchFilter();
    }


    private void loadMedecins() throws SQLException {
        medecinList.clear(); // Clear existing data (if any)

        // Fetch the list of doctors from the DAO
        medecinList.addAll(MedecinDAO.getAllMedecin());

        // Set the list in the TableView
        MedecinTable.setItems(medecinList);

         //Initialize columns
        mednameCol.setCellValueFactory(new PropertyValueFactory<>("nom")); // Ensure "nom" matches Medecin class attribute
        serviceCol.setCellValueFactory(new PropertyValueFactory<>("service")); // Ensure "specialite" matches Medecin class attribute
        //serviceCol.setCellValueFactory(cellData -> cellData.getValue().getService().asObject());
    }



    @FXML
    private ComboBox<String> specialtyComboBox;

    private void loadSpecialties() {
        specialtyComboBox.getItems().clear(); // Nettoyer le ComboBox avant de charger les nouvelles valeurs

        // Récupérer les spécialités depuis la base de données via MedecinDAO
        specialtyComboBox.getItems().addAll(MedecinDAO.getAllSpecialties());
    }


    private void setupSearchFilter() {
        FilteredList<Medecin> filteredData = new FilteredList<>(medecinList, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter(filteredData);
        });

        specialtyComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter(filteredData);
        });

        // Trier les résultats
        SortedList<Medecin> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(MedecinTable.comparatorProperty());

        // Appliquer les données filtrées à la TableView
        MedecinTable.setItems(sortedData);
    }

    private void applyFilter(FilteredList<Medecin> filteredData) {
        String searchText = searchField.getText();
        String selectedSpecialty = specialtyComboBox.getValue();

        filteredData.setPredicate(medecin -> {
            boolean matchesSearch = (searchText == null || searchText.trim().isEmpty()) ||
                    medecin.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                    medecin.getService().toLowerCase().contains(searchText.toLowerCase());

            boolean matchesSpecialty = (selectedSpecialty == null || selectedSpecialty.trim().isEmpty()) ||
                    medecin.getService().equalsIgnoreCase(selectedSpecialty);

            return matchesSearch && matchesSpecialty;
        });
    }



@FXML
private void handleMedecinSelection() {
    Medecin selectedMedecin = (Medecin) MedecinTable.getSelectionModel().getSelectedItem();

    if (selectedMedecin != null) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Prendre un rendez-vous");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous prendre un rendez-vous avec " + selectedMedecin.getNom() + " ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            sendRDVRequest(selectedMedecin.getId(), selectedMedecin.getEmail(), selectedMedecin.getNumero());
        }
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aucun médecin sélectionné");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez sélectionner un médecin avant de demander un rendez-vous.");
        alert.showAndWait();
    }
}

    private void sendRDVRequest(int medecinId, String medecinMail, String medecinNum) {
        int patientId = UserSession.getId(); // Récupérer l'ID du patient connecté
        try {
            boolean success = MedecinDAO.sendRDVRequest(medecinId, patientId);

            if (success) {
                EmaliSender.sendEmail(medecinMail, "Vous avez une nouvelle demande de rendez-vous");
                SmsSender.sendSms(medecinNum, "Vous avez une nouvelle demande de rendez-vous");
                Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                confirmation.setTitle("Demande envoyée");
                confirmation.setHeaderText(null);
                confirmation.setContentText("Votre demande de rendez-vous a été envoyée !");
                confirmation.showAndWait();
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Erreur");
                error.setHeaderText(null);
                error.setContentText("Une erreur s'est produite lors de l'envoi de votre demande.");
                error.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Erreur SQL");
            error.setHeaderText(null);
            error.setContentText("Problème lors de l'accès à la base de données.");
            error.showAndWait();
        }
    }



    public void logout(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/hello-view.fxml", "Login", false);
        UserSession.logout();
    }
}
