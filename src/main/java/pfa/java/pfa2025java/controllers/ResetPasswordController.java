package pfa.java.pfa2025java.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pfa.java.pfa2025java.EmaliSender;
import pfa.java.pfa2025java.PasswordGenerator;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.dao.ResetPasswordDAO;
import pfa.java.pfa2025java.dao.UserDAO;
import pfa.java.pfa2025java.model.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.Properties;

import static pfa.java.pfa2025java.EmaliSender.sendEmail;


public class ResetPasswordController {
    public VBox contianerDemande;
    public TextField codeField;
    public VBox resetVbx;
    public TextField newpass;
    public TextField newpass1;
    @FXML
    private TextField emailField; // TextField for the user's email
    private String password = PasswordGenerator.generateTemporaryPassword();
    private String messagetosend = "Voici Votre code  :" + password;

    public void initialize() {
        contianerDemande.setVisible(false);
        resetVbx.setVisible(false);
    }

    @FXML
    private void handleSendPassword() throws SQLException {
        String userEmail = emailField.getText().trim(); // Get the email from the TextField
        resetVbx.setVisible(false);
        contianerDemande.setVisible(false);
        if (userEmail.isEmpty() || UserDAO.getUserByEmail(userEmail) == null) {
            showAlert("Error", "Compte n'existe pas doit exister");
            return;
        }

        // Send the email
        boolean emailSent = EmaliSender.sendEmail(userEmail, messagetosend);


        if (emailSent) {
            ResetPasswordDAO.createDemande(userEmail, password);

            contianerDemande.setVisible(true);
            showAlert("Success", "Verifier votre Boite Email vous y trouverrais un code ");

        } else {
            showAlert("Error", "Erreur d'envoi");
        }
    }

    // Method to send an email


    // Utility method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handledmenade(ActionEvent actionEvent) throws SQLException {
        resetVbx.setVisible(false);
        String code = codeField.getText();
        String email = emailField.getText();
        if (ResetPasswordDAO.checkCode(code, email)) {
            ResetPasswordDAO.used(code,email);
            resetVbx.setVisible(true);
        }
    }

    public void reset(ActionEvent actionEvent) throws SQLException {
        User user = UserDAO.getUserByEmail(emailField.getText());
        if (newpass.getText().equals(newpass1.getText()) && newpass.getText().length() >= 8) {
            UserDAO.changePassword(newpass.getText(), user.getId());
            showAlert("Demande", "Mot de passe renitialisee avec succes");
            Stage stage = (Stage) resetVbx.getScene().getWindow();
            stage.close();
        } else {
            showAlert("Demande", "Verifier votre mot de passe et sa longueur");
        }
    }
}
