package pfa.java.pfa2025java.controllers.medecin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.dao.TaskDAO;
import pfa.java.pfa2025java.model.Task;
import pfa.java.pfa2025java.model.TaskStatus;
import pfa.java.pfa2025java.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TaskController {

    private final int userId = UserSession.getId();
    @FXML private TextField taskTitle;
    @FXML private DatePicker deadlinePicker;
    @FXML private TextField searchUser;
    @FXML private ListView<User> userListView;
    @FXML private TextArea description;

    // Données
    private final TaskDAO taskDAO = new TaskDAO();
    private User selectedUser;

    @FXML
    public void initialize() {
        setupDatePicker();
        setupUserSearch();
    }

    private void setupDatePicker() {
        deadlinePicker.setValue(LocalDate.now());
        deadlinePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.now()));
            }
        });
    }

    private void setupUserSearch() {
        // Configuration de la liste déroulante
        userListView.setVisible(false);
        userListView.setPrefHeight(100);

        // Gestion de la saisie utilisateur
        searchUser.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                userListView.setVisible(false);
                return;
            }
            searchAndDisplayUsers(newVal);
        });

        // Gestion de la sélection
        userListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                handleUserSelection(newVal);
            }
        });
    }

    private void searchAndDisplayUsers(String searchTerm) {
        try {
            List<User> users = taskDAO.searchMedicalStaff(searchTerm);
            updateUserListView(users);
        } catch (SQLException e) {
            showError("Erreur de base de données", e.getMessage());
        }
    }

    private void updateUserListView(List<User> users) {
        userListView.setItems(FXCollections.observableArrayList(users));
        userListView.setVisible(!users.isEmpty());
        userListView.getSelectionModel().clearSelection(); // Add this line

        userListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? null : String.format("%s %s (%s)",
                        user.getPrenom(),
                        user.getNom(),
                        user.getRole()));
            }
        });
    }

    private void handleUserSelection(User user) {
        selectedUser = user;
        searchUser.setText(String.format("%s %s", user.getPrenom(), user.getNom()));
        userListView.setVisible(false);
    }

    @FXML
    private void handleCreateTask() {
        if (!validateInput()) return;

        try {
            Task task = createTaskFromInput();
            if (taskDAO.insertTask(task)) {
                showSuccess("Tâche créée avec succès!");
                clearForm();
            }
        } catch (SQLException e) {
            showError("Erreur d'enregistrement", e.getMessage());
        }
    }

    private Task createTaskFromInput() {
        Task task = new Task();
        task.setTitle(taskTitle.getText());
        task.setDescription(description.getText());
        task.setAssignedTo(selectedUser.getId());
        task.setCreatedBy(userId);
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDate.now());
        task.setDeadline(deadlinePicker.getValue());
        return task;
    }

    private boolean validateInput() {
        if (taskTitle.getText().isBlank()) {
            showWarning("Titre manquant", "Veuillez saisir un titre pour la tâche");
            return false;
        }

        if (selectedUser == null) {
            showWarning("Utilisateur non sélectionné", "Veuillez sélectionner un utilisateur");
            return false;
        }

        if (deadlinePicker.getValue() == null || deadlinePicker.getValue().isBefore(LocalDate.now())) {
            showWarning("Date invalide", "Veuillez sélectionner une date valide");
            return false;
        }

        return true;
    }

    private void clearForm() {
        taskTitle.clear();
        description.clear();
        searchUser.clear();
        deadlinePicker.setValue(LocalDate.now());
        selectedUser = null;
        userListView.getItems().clear();
    }

    // Méthodes d'affichage des alertes
    private void showSuccess(String message) {
        showAlert("Succès", message, Alert.AlertType.INFORMATION);
    }

    private void showWarning(String title, String message) {
        showAlert(title, message, Alert.AlertType.WARNING);
    }

    private void showError(String title, String message) {
        showAlert(title, message, Alert.AlertType.ERROR);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void loadPage(String fxmlPath, Node sourceNode) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur de chargement de la page: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // Handlers pour les boutons du menu
    @FXML
    private void handleCreateTaskPage() {
        loadPage("/vues/task-view.fxml", taskTitle); // taskTitle est un composant quelconque de la scène
    }

    @FXML
    private void handleMyTasksPage() {
        loadPage("/vues/my-tasks-view.fxml", taskTitle);
    }

    @FXML
    private void handleFollowedTasksPage() {
        loadPage("/vues/followed-tasks-view.fxml", taskTitle);
    }
}