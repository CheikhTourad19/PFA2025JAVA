package pfa.java.pfa2025java.controllers.patient;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.controllers.InsrciptionController;
import pfa.java.pfa2025java.model.PasswordUtils;
import pfa.java.pfa2025java.dao.UserDAO;

import java.sql.SQLException;

public class ProfilController {
    public TextField nom;
    public TextField prenom;
    public TextField numero;
    public TextField emailField;
    public PasswordField oldpasswordField;
    public PasswordField newpasswordField;
    public PasswordField newpasswordFieldConfirmed;

    public void initialize() {
        nom.setText(UserSession.getNom());
        prenom.setText(UserSession.getPrenom());
        numero.setText(UserSession.getNumero());
        emailField.setText(UserSession.getEmail());
    }

    public void consulterOrdonnances(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/ordonnance-view.fxml", "Ordonnances", false);
    }

    public void demandeRendezVous(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/demanderdv-view.fxml", "Rendez-vous", false);
    }

    public void consulterPharmacies(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/pharmacie-view.fxml", "Pharmacies", false);
    }

    public void consulterRdv(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/accueil-view.fxml", "Rendez-vous", false);
    }


    public void logout(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/hello-view.fxml", "Login", false);
        UserSession.logout();
    }

    public void updateProfile(ActionEvent actionEvent) throws SQLException {
        Alert alert;
        if (!oldpasswordField.getText().isEmpty() && PasswordUtils.checkPassword(oldpasswordField.getText(), UserSession.getPassword())) {
            if (newpasswordField.getText().equals(newpasswordFieldConfirmed.getText()) && !newpasswordField.getText().isEmpty() && newpasswordField.getText().length() >= 8) {
                boolean updatedPass = UserDAO.changePassword(newpasswordField.getText() , UserSession.getId());
                if (updatedPass) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("mot de passe a bien ete mise a jour");
                    alert.setTitle("Succes");
                    alert.setHeaderText(null);
                    alert.show();
                }
            } else if ((!newpasswordField.getText().isEmpty() && newpasswordField.getText().length() <= 8) || !newpasswordField.getText().equals(newpasswordFieldConfirmed.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("les mots de passe ne se corresponde pas ou ne comporte pas 8 lettre ");
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.show();
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("l'ancien mot de passe n'est pas valide ");
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.show();

        }

        if (InsrciptionController.isValidEmail(emailField.getText())) {
            boolean updatemail = UserDAO.updateEmail(emailField.getText());
            if (updatemail) {
                UserSession.setEmail(emailField.getText());
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("l'adresse mail a bien ete mise a jour");
                alert.setTitle("Succes");
                alert.setHeaderText(null);
                alert.show();
            }

        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("email n'est pas valide ");
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.show();
        }

        if (!nom.getText().isEmpty() && !prenom.getText().isEmpty() && !numero.getText().isEmpty()) {
            boolean updateNomPrenomNum = UserDAO.changePrenomNomNumero(prenom.getText(), nom.getText(), numero.getText());
            if (updateNomPrenomNum) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("nom,prenom et numero a bien ete mise a jour");
                alert.setTitle("Succes");
                alert.setHeaderText(null);
                alert.show();
                UserSession.setPrenom(prenom.getText());
                UserSession.setNom(nom.getText());
                UserSession.setNumero(numero.getText());
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("erreur lors de la modification des nom,prenom et numero ");
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.show();
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("vous ne pouvez pas modifier les champs vides ");
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.show();
        }
    }
}
