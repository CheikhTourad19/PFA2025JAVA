package pfa.java.pfa2025java.controllers.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pfa.java.pfa2025java.HelloApplication;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.model.User;
import pfa.java.pfa2025java.model.UserDAO;

import java.io.IOException;
import java.util.Objects;

public class UsersController {
    @FXML
    protected AnchorPane root;
    @FXML
    private TableView<User> usersTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> nomColumn;
    @FXML private TableColumn<User, String> prenomColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, Void> actionsColumn;
    @FXML private MenuButton menuButton;
    private final ObservableList<User> userList = FXCollections.observableArrayList();

    public void initialize() {
        // Set up columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Load data
        loadUsers();

        // Add buttons in the actions column
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                editButton.setOnAction(event -> handleEditUser(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleDeleteUser(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });

    }

    private void loadUsers() {
        userList.setAll(UserDAO.getAllUsers());
        usersTable.setItems(userList);
    }



    private void handleEditUser(User user) {
        System.out.println("Modifier utilisateur: " + user.getNom());
        // Open Edit User form (implement later)
    }



    private void handleDeleteUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous supprimer " + user.getNom() + " ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                UserDAO.deleteUser(user.getId());
                loadUsers();
            }
        });
    }


    public void handleAddUser(ActionEvent event) throws IOException {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(root,"/pfa/java/pfa2025java/views/Admin/addUser.fxml","ajouter utilisateur",false);
    }
}
