package pfa.java.pfa2025java.controllers.pharmacie;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.Medicament;
import pfa.java.pfa2025java.dao.MedicamentDAO;

import java.io.FileWriter;
import java.io.IOException;

import java.util.List;

public class StockController {
    public Button accueilButton;
    public PieChart stockPieChart;
    public ProgressIndicator loading;
    public Text name;
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
    @FXML
    private TableView listeMedicamentStock;
    private final ObservableList<Medicament> medicamentList = FXCollections.observableArrayList();
    @FXML
    private TextField searchField; // Add this for the search field

    private FilteredList<Medicament> filteredData;


    @FXML
    public void initialize() {
        name.setText(UserSession.getNom() + " " + UserSession.getPrenom());
        loading.setVisible(true);
        Task<Void> backgroundTask = new Task<>() {
            @Override
            protected Void call()  {
                loadMedicaments();
                return null;
            }
            @Override
            protected void succeeded() {
                loading.setVisible(false);
            }
            @Override
            protected void failed() {
                loading.setVisible(false);
            }

        };

        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        prixColumn.setCellValueFactory(cellData -> cellData.getValue().prixProperty().asObject());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        stock.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        // Utilisation de RowFactory pour personnaliser le style de la ligne entière

        listeMedicamentStock.setRowFactory(tv -> {
            TableRow<Medicament> row = new TableRow<>();
            row.itemProperty().addListener((observable, oldItem, newItem) -> {
                if (newItem != null && newItem.getStock() < 10) {
                    row.setStyle("-fx-background-color: #FF5733; -fx-text-fill: white;");
                } else {
                    row.setStyle("");  // réinitialise le style si le stock est suffisant
                }
            });
            return row;
        });

        new Thread(backgroundTask).start();

        // Initialize FilteredList with the full list
        filteredData = new FilteredList<>(medicamentList, p -> true);

        // Add a listener to searchField
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(medicament -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                return true; // Show all if search is empty
            }
            String lowerCaseFilter = newValue.toLowerCase();
            return medicament.getNom().toLowerCase().contains(lowerCaseFilter);
        }));

        // Wrap FilteredList in a SortedList and bind it to TableView
        SortedList<Medicament> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(listeMedicamentStock.comparatorProperty());
        listeMedicamentStock.setItems(sortedData);

    }

    private void loadMedicaments() {
        List<Medicament> medicaments = MedicamentDAO.getMedicamentsByPharmacie();

        Platform.runLater(() -> {
            medicamentList.setAll(medicaments);
            updateStockChart();
            checkLowStock(medicamentList);
        });
    }


    public void addstock() {
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

        boolean success = MedicamentDAO.ajouterStock(selectedMedicament.getId(), increment, UserSession.getId());

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Stock mis à jour avec succès.");
            loadMedicaments();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour du stock.");
        }
        updateStockChart();
    }

    public void addmecidament() {
        String nom = nomField.getText();
        String description = descriptionFiled.getText();
        String prix = prixField.getText();

        if (nom.isEmpty() || description.isEmpty() || prix.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        boolean success = MedicamentDAO.addMedicamentWithStock(nom, description, prix, UserSession.getId());

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Médicament ajouté avec succès.");
            loadMedicaments();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout du médicament.");
        }
        updateStockChart();
    }

    private void showAlert(Alert.AlertType alertType, String erreur, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle(erreur);
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

    public void clearSearch() {
        searchField.clear();
    }

    private void updateStockChart() {
        if (stockPieChart.getData().isEmpty()) {
            // Première initialisation
            for (Medicament med : medicamentList) {
                stockPieChart.getData().add(new PieChart.Data(med.getNom(), med.getStock()));
            }
        } else {
            // Mise à jour des données existantes
            for (PieChart.Data data : stockPieChart.getData()) {
                for (Medicament med : medicamentList) {
                    if (data.getName().equals(med.getNom())) {
                        data.setPieValue(med.getStock());
                    }
                }
            }
        }
    }

    public static void checkLowStock(List<Medicament> medicamentList) {
        StringBuilder message = new StringBuilder();

        for (Medicament med : medicamentList) {
            if (med.getStock() < 10) {
                message.append("Le médicament ").append(med.getNom()).append(" est presque épuisé !\n");
            }
        }

        if (!message.isEmpty()) {
            showNotification(message.toString());
        }
    }

    private static void showNotification( String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();

        // Ajout d'un son d'alerte
        java.awt.Toolkit.getDefaultToolkit().beep();
    }


    @FXML
    private void exportToExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier CSV", "*.csv"));
        fileChooser.setTitle("Enregistrer le fichier");
        fileChooser.setInitialFileName(UserSession.getNom() + ".csv");

        java.io.File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.append("Nom,Prix,Description,Stock\n");
                for (Medicament med : medicamentList) {
                    writer.append(med.getNom()).append(",");
                    writer.append(String.valueOf(med.getPrix())).append(",");
                    writer.append(med.getDescription()).append(",");
                    writer.append(String.valueOf(med.getStock())).append("\n");
                }
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Exportation réussie !");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation !");
            }
        }
    }

}
