package pfa.java.pfa2025java.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pfa.java.pfa2025java.SwtichScene;

public class InsrciptionController {
    public PasswordField passwordField;
    public PasswordField confirmPasswordField;
    public TextField emailField;
    public Button signupButton;
    public TextField prenom;
    public TextField nom;
    public Text message;
    public Button loginButton;

    public void saveUser() {

        Alert message = new Alert(Alert.AlertType.INFORMATION);
        message.setHeaderText("Succes ");
        message.setContentText("Votre Compte a ete cree avec succes");
        message.showAndWait();
        if (message.getResult() == ButtonType.OK) {
            SwtichScene swtichScene = new SwtichScene();
            swtichScene.loadScene(nom,"views/hello-view.fxml","login",false);
        }

    }
    public void handleSignup(ActionEvent actionEvent) {
        if (nom.getText().isEmpty() || prenom.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                confirmPasswordField.getText().isEmpty()) {
            message.setText("Veuillez remplir tous les champs");
        } else if (passwordField.getText().equals(confirmPasswordField.getText())) {
            saveUser();
        } else
            message.setText("Les mots de passe ne correspondent pas");
    }

    public void login(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/hello-view.fxml","login",false);
    }
}
