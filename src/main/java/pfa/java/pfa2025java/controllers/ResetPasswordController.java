package pfa.java.pfa2025java.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import pfa.java.pfa2025java.PasswordGenerator;
import pfa.java.pfa2025java.dao.UserDAO;
import pfa.java.pfa2025java.model.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.Properties;


public class ResetPasswordController {
    @FXML
    private TextField emailField; // TextField for the user's email
    private String password = PasswordGenerator.generateTemporaryPassword();

    @FXML
    private void handleSendPassword() throws SQLException {
        String userEmail = emailField.getText().trim(); // Get the email from the TextField

        if (userEmail.isEmpty() || UserDAO.getUserByEmail(userEmail) == null) {
            showAlert("Error", "Compte n'existe pas doit exister");
            return;
        }

        // Send the email
        boolean emailSent = sendEmail(userEmail);
        User user = UserDAO.getUserByEmail(userEmail);

        if (emailSent) {
            UserDAO.changePassword(password, user.getId());
            showAlert("Success", "Verifier votre Boite Email vous y trouverrai un mot de passe tamporaire");
        } else {
            showAlert("Error", "Erreur d'envoi");
        }
    }

    // Method to send an email
    private boolean sendEmail(String toEmail) {
        final String username = "elghothvadel@gmail.com"; // Your email address
        final String appPassword = "mjzf sfqp kcsh ytke"; // Your email app password

        // Set up mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);

            // Set the sender and recipient addresses
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

            // Set the email subject and body
            message.setSubject("Email de E-Medical ");
            message.setText("Voici un mot de passe tamporaire que vous allez utiliser modifier le au plus vite  :  " + password);

            // Send the email
            Transport.send(message);
            return true; // Email sent successfully

        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // Email failed to send
        }
    }

    // Utility method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
