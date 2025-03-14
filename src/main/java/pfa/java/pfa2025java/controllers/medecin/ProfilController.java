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
            } else {
                System.out.println("Échec de l'enregistrement de l'image.");
            }
        } else {
            System.out.println("Aucune image sélectionnée.");
        }


    }

    @FXML
    private void loginWithFacialRecognition() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reconnaissance facial");
        Task task = new Task() {
            @Override
            protected User call() throws Exception {
                // Capture image from webcam
                Mat capturedImage = captureImage();
                if (capturedImage == null) {
                    Platform.runLater(() -> {
                        alert.setContentText("Erreur lors de la image");
                        alert.show();
                    });
                    return null;

                }

                Rect faceRect = detectFace(capturedImage);
                if (faceRect == null) {
                    Platform.runLater(() -> {
                        alert.setContentText("Aucun visage detecte");
                        alert.show();
                    });
                    return null;
                }

                // Crop the detected face
                Mat detectedFace = new Mat(capturedImage, faceRect);

                // Convert the detected face to a byte array
                BytePointer buf = new BytePointer();
                opencv_imgcodecs.imencode(".jpg", detectedFace, buf);
                long size = buf.limit();
                byte[] capturedFacialData = new byte[(int) size];
                buf.get(capturedFacialData);
                User user = new User();
                // Retrieve the user by comparing facial data
                if (UserDAO.saveUserFacialData(UserSession.getId(), capturedFacialData)) {
                    return user;
                }
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Reconnaissance facial");
                    alert.setHeaderText(null);
                    alert.setContentText("Succes");
                    alert.show();
                });
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Reconnaissance facial");
                    alert.setHeaderText(null);
                    alert.setContentText("erreur");
                    alert.show();
                });

            }
        };

        new Thread(task).start();
    }
    public Mat captureImage() {
        VideoCapture capture = new VideoCapture(0); // 0 for default webcam
        Mat frame = new Mat();

        // Check if the camera is opened successfully
        if (!capture.isOpened()) {
            System.out.println("Erreur: Impossible d'ouvrir la caméra.");
            return null;
        }

        // Capture a frame
        if (capture.read(frame)) {
            System.out.println("Image capturée avec succès.");
        } else {
            System.out.println("Erreur: Impossible de capturer l'image.");
            frame = null;
        }

        // Release the camera
        capture.release();
        return frame;
    }

    public Rect detectFace(Mat image) {
        CascadeClassifier faceDetector = loadCascadeClassifier();
        if (faceDetector == null) {
            System.out.println("Erreur: Impossible de charger le fichier Haar Cascade.");
            return null;
        }

        RectVector faceDetections = new RectVector();
        faceDetector.detectMultiScale(image, faceDetections);

        if (faceDetections.size() > 0) {
            return faceDetections.get(0);
        }

        return null;
    }

    private CascadeClassifier loadCascadeClassifier() {
        try {
            // Load the Haar Cascade file from resources
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("haarcascade_frontalface_default.xml");
            if (inputStream == null) {
                throw new IOException("Fichier Haar Cascade introuvable dans les ressources.");
            }

            // Create a temporary file to store the Haar Cascade file
            File tempFile = File.createTempFile("haarcascade_frontalface_default", ".xml");
            tempFile.deleteOnExit(); // Delete the file when the JVM exits

            // Copy the Haar Cascade file from resources to the temporary file
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Load the Haar Cascade file into the CascadeClassifier
            CascadeClassifier faceDetector = new CascadeClassifier(tempFile.getAbsolutePath());
            if (faceDetector.empty()) {
                throw new IOException("Échec du chargement du fichier Haar Cascade.");
            }

            return faceDetector;
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier Haar Cascade: " + e.getMessage());
            return null;
        }
    }


}


