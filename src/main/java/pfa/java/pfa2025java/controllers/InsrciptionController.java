package pfa.java.pfa2025java.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class InsrciptionController {
    public PasswordField passwordField;
    public PasswordField confirmPasswordField;
    public TextField emailField;
    public Button signupButton;
    public TextField prenom;
    public TextField nom;
    public Text message;

    public void handleSignup(ActionEvent actionEvent) {
        if (nom.getText().isEmpty() || prenom.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                confirmPasswordField.getText().isEmpty()) {
            message.setText("Veuillez remplir tous les champs");
        } else if (passwordField.getText().equals(confirmPasswordField.getText())) {

        } else
            message.setText("Les mots de passe ne correspondent pas");
    }
}
