package pfa.java.pfa2025java.controllers.medecin;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.controllers.InsrciptionController;
import pfa.java.pfa2025java.model.PasswordUtils;
import pfa.java.pfa2025java.dao.UserDAO;
import pfa.java.pfa2025java.model.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

public class ProfilController {

    public TextField nom;
    public TextField prenom;
    public TextField numero;
    public TextField emailField;
    public PasswordField oldpasswordField;
    public PasswordField newpasswordField;
    public PasswordField newpasswordFieldConfirmed;
    @FXML
    private ImageView profileImageView;

    private File selectedImageFile;
    private final String DEFAULT_IMAGE_PATH = "/pfa/java/pfa2025java/assets/profil.png";


    public void initialize() {
        nom.setText(UserSession.getNom());
        prenom.setText(UserSession.getPrenom());
        numero.setText(UserSession.getNumero());
        emailField.setText(UserSession.getEmail());
        loadImageFromDatabase();
    }


    private void loadImageFromDatabase() {
        InputStream is = UserDAO.getUserImage(UserSession.getId());
        if (is != null) {
            profileImageView.setImage(new Image(is));
        }
    }

    @FXML
    private void handleImageUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedImageFile = file;
            profileImageView.setImage(new Image(file.toURI().toString()));
        }
    }


    public void updateProfile(ActionEvent actionEvent) throws SQLException {
        Alert alert;
        if (!oldpasswordField.getText().isEmpty() && PasswordUtils.checkPassword(oldpasswordField.getText(), UserSession.getPassword())) {
            if (newpasswordField.getText().equals(newpasswordFieldConfirmed.getText()) && !newpasswordField.getText().isEmpty() && newpasswordField.getText().length() >= 8) {
                boolean updatedPass = UserDAO.changePassword(newpasswordField.getText(), UserSession.getId());
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
        if (selectedImageFile != null) {
            boolean success = UserDAO.saveUserImage(UserSession.getId(), selectedImageFile);
            if (success) {
                System.out.println("Image enregistrée avec succès.");
                loadImageFromDatabase(); // Reload the image to ensure it updates
            } else {
                System.out.println("Échec de l'enregistrement de l'image.");
            }
        } else {
            System.out.println("Aucune image sélectionnée.");
        }


    }




}


