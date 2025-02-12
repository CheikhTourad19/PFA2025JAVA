package pfa.java.pfa2025java.controllers.pharmacie;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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

    public Button stockButton;
    public Button ordonnanceButton;
    public Text name;
    public TextField streetField;
    public TextField cityField;
    public TextField neighborhoodField;
    public TextField emailField;
    public PasswordField oldpasswordField;
    public PasswordField newpasswordField;
    public PasswordField newpasswordFieldConfirmed;

    public void initialize() throws SQLException {
        name.setText(UserSession.getPrenom() + " " + UserSession.getNom());
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

    public void updateProfile(ActionEvent actionEvent) throws SQLException {
        if (newpasswordField.getText().equals(newpasswordFieldConfirmed.getText()) && !newpasswordField.getText().isEmpty() && newpasswordField.getText().length()>=8) {
            if (PasswordUtils.checkPassword(oldpasswordField.getText(),UserSession.getPassword())) {
                if (UserDAO.changePassword(newpasswordField.getText())){
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
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("les mots de passe ne se corresponde pas ou ne comporte pas 8 lettre ");
            alert.show();
        }
        boolean updated = PharmacieDAO.UpdatePharmacieAdresse(streetField.getText(),cityField.getText(),neighborhoodField.getText());

        if (updated) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setHeaderText(null);
            alert.setContentText("l'adresse a bien ete mise a jour");
            alert.show();

        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("l'adresse n'a pas pu etre saisi");
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
