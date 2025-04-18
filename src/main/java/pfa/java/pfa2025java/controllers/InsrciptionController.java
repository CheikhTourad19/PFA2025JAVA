package pfa.java.pfa2025java.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.dao.UserDAO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsrciptionController {
    public PasswordField passwordField;
    public PasswordField confirmPasswordField;
    public TextField emailField;
    public Button signupButton;
    public TextField prenom;
    public TextField nom;
    public Text message;
    public Button loginButton;
    public TextField numero;

    public void initialize() {
    }
    public void saveUser() {
        boolean log = UserDAO.registerUser(nom.getText(), emailField.getText(), passwordField.getText(), prenom.getText(), numero.getText(),"patient");
        if (log) {
            Alert message = new Alert(Alert.AlertType.INFORMATION);
            message.setHeaderText("Succes ");
            message.setContentText("Votre Compte a ete cree avec succes");
            message.showAndWait();
            if (message.getResult() == ButtonType.OK) {
                SwtichScene swtichScene = new SwtichScene();
                swtichScene.loadScene(nom, "views/hello-view.fxml", "login", false);
            }
        } else {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.setHeaderText("Erreur ");
            message.setContentText("Votre Compte n'a pas ete cree");
            message.showAndWait();
        }


    }
    public void handleSignup(ActionEvent actionEvent) {
        if (nom.getText().isEmpty() || prenom.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                confirmPasswordField.getText().isEmpty()) {
            message.setText("Veuillez remplir tous les champs");
        } else if (passwordField.getText().equals(confirmPasswordField.getText())) {
            if (passwordField.getText().length() < 8) {
                message.setText("il faut au moins 8 caracteres");
                passwordField.styleProperty().setValue("-fx-border-color: red");
                confirmPasswordField.styleProperty().setValue("-fx-border-color: red");
            } else {
                if (!isValidEmail(emailField.getText())) {
                    message.setText("Email invalide");
                    emailField.styleProperty().setValue("-fx-border-color: red");
                } else if (numero.getText().length() != 8) {
                    message.setText("Numero de telephone invalide");
                    numero.styleProperty().setValue("-fx-border-color: red");
                } else {
                    saveUser();
                }

            }
        } else
            message.setText("Les mots de passe ne correspondent pas");
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public void login(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/hello-view.fxml", "Login", false);
    }
}
