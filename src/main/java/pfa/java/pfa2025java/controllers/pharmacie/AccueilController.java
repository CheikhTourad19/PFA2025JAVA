package pfa.java.pfa2025java.controllers.pharmacie;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.*;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccueilController {


    @FXML
    private Text name;
    @FXML
    private TextField streetField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField neighborhoodField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField oldpasswordField;
    @FXML
    private PasswordField newpasswordField;
    @FXML
    private PasswordField newpasswordFieldConfirmed;


    public void initialize() {
        name.setText(UserSession.getNom() + " " + UserSession.getPrenom());
        Task<Void> backgroundTask = new Task<>() {
            @Override
            protected Void call() throws SQLException {
                Adresse adresse = PharmacieDAO.getPharmacieAdresse();
                if (adresse != null) {
                    streetField.setText(adresse.getRue());
                    cityField.setText(adresse.getVille());
                    neighborhoodField.setText(adresse.getQuartier());
                }
                return null;
            }
        };

        new Thread(backgroundTask).start();

        emailField.setText(UserSession.getEmail());

    }

    public void gotostock(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/pharmacie/stock-view.fxml","Stock",false);
    }

    public void gotoordonnance(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/pharmacie/ordonnance-view.fxml","Ordonnance",false);
    }

    public void logout(ActionEvent actionEvent) {

        UserSession.logout();
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent,"views/hello-view.fxml","Login",false);
    }

    public void updateProfile() throws SQLException {
        if (newpasswordField.getText().equals(newpasswordFieldConfirmed.getText()) && !newpasswordField.getText().isEmpty() && newpasswordField.getText().length()>=8) {
            if (PasswordUtils.checkPassword(oldpasswordField.getText(),UserSession.getPassword())) {
                if (UserDAO.changePassword(newpasswordField.getText(),UserSession.getId())){
                    UserSession.setPassword(PasswordUtils.hashPassword(newpasswordField.getText()));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Succes");
                    alert.setHeaderText(null);
                    alert.setContentText("mot de passe a bien ete mise a jour");
                    alert.show();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("l'ancien mot de passe n'est pas valide ");
                alert.show();
            }
        }else if ((!newpasswordField.getText().isEmpty() && newpasswordField.getText().length()<=8) || !newpasswordField.getText().equals(newpasswordFieldConfirmed.getText()) ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("les mots de passe ne se corresponde pas ou ne comporte pas 8 lettre ");
            alert.show();
        }
        boolean updatedPass = PharmacieDAO.UpdatePharmacieAdresse(streetField.getText(),cityField.getText(),neighborhoodField.getText());

        Alert alert;
        if (updatedPass) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setHeaderText(null);
            alert.setContentText("l'adresse a bien ete mise a jour");

        }else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("l'adresse n'a pas pu etre saisi");
        }
        alert.show();

        if (!emailField.getText().isEmpty() && isValidEmail(emailField.getText())) {
            boolean updatemail=UserDAO.updateEmail(emailField.getText());
            if (updatemail) {
                UserSession.setEmail(emailField.getText());

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succes");
                alert.setHeaderText(null);
                alert.setContentText("l'adresse mail a bien ete mise a jour");
                alert.show();
            }else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("errur lors de la modification du mail ");
                alert.show();
            }
        } else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("email n'est pas valide ");
            alert.show();
        }

    }
    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
