package pfa.java.pfa2025java.controllers.Admin;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pfa.java.pfa2025java.dao.MedecinDAO;
import pfa.java.pfa2025java.model.User;
import pfa.java.pfa2025java.dao.UserDAO;

import static pfa.java.pfa2025java.controllers.InsrciptionController.isValidEmail;

public class AddMedecinController {
    public TextField prenom;
    public TextField nom;
    public TextField emailField;
    public TextField numero;
    public PasswordField passwordField;
    public PasswordField confirmPasswordField;
    public Button signupButton;
    public Text message;
    public TextField service;


    public void saveUser() {
        boolean log = UserDAO.registerUser(nom.getText(), emailField.getText(), passwordField.getText(), prenom.getText(), numero.getText(),"medecin");
        if (log) {
            User medecin=UserDAO.getUserByEmail(emailField.getText());
            if (MedecinDAO.addMedecin(medecin,service.getText())){
                Alert message = new Alert(Alert.AlertType.INFORMATION);
                message.setHeaderText("Succes ");
                message.setContentText("Votre Compte a ete cree avec succes");
                message.showAndWait();
                if (message.getResult() == ButtonType.OK) {
                    Stage stage = (Stage) service.getScene().getWindow();
                    stage.close();
                }

            }else  {
                Alert message = new Alert(Alert.AlertType.ERROR);
                message.setHeaderText("Erreur ");
                message.setContentText("Votre Compte n'a pas ete cree");
                message.showAndWait();            }

        } else {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.setHeaderText("Erreur ");
            message.setContentText("Votre Compte n'a pas ete cree");
            message.showAndWait();
        }




    }
    public void handleSignup(ActionEvent actionEvent) {
        if (nom.getText().isEmpty() || prenom.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                confirmPasswordField.getText().isEmpty()  || service.getText().isEmpty()) {
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
}
