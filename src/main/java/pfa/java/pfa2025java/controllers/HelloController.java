package pfa.java.pfa2025java.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import pfa.java.pfa2025java.OpenCVUtils;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.User;
import pfa.java.pfa2025java.dao.UserDAO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.prefs.Preferences;

public class HelloController {
    public ProgressIndicator loading;
    @FXML
    private Text loginresult;
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    static {
        OpenCVUtils.initialize(); // Initialize OpenCV
    }

    @FXML
    public void initialize() {
        password.setOnKeyPressed(this::handleEnterKey);
        username.setOnKeyPressed(this::handleEnterKey);
        loading.setVisible(false);
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        String last_seremail = prefs.get("last_seremail", "");
        if (!last_seremail.isEmpty()) {
            username.setText(last_seremail);
        }
    }

    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            login();
        }
    }

    @FXML
    public void login() {
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            loginresult.setText("Username ou password vide");
            loginresult.setStyle("-fx-text-fill: red;");
            return;
        }

        loading.setVisible(true);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                boolean isValid = UserDAO.login(username.getText(), password.getText());

                Platform.runLater(() -> {
                    if (isValid) {
                        User user = UserDAO.getUserByEmail(username.getText());
                        if (user == null) {
                            loginresult.setText("Erreur: utilisateur introuvable !");
                            loginresult.setStyle("-fx-text-fill: red;");
                            loading.setVisible(false);
                            return;
                        }

                        // Stocker la session utilisateur
                        UserSession.setEmail(user.getEmail());
                        UserSession.setPrenom(user.getPrenom());
                        UserSession.setNom(user.getNom());
                        UserSession.setId(user.getId());
                        UserSession.setRole(user.getRole());
                        UserSession.setPassword(user.getPassword());
                        UserSession.setNumero(user.getNumero());

                        // Sauvegarder l'email dans les préférences
                        Preferences prefs = Preferences.userNodeForPackage(getClass());
                        prefs.put("last_seremail", user.getEmail());

                        // Changer de scène en fonction du rôle
                        SwtichScene swtichScene = new SwtichScene();
                        switch (user.getRole()) {
                            case "pharmacie" ->
                                    swtichScene.loadScene(password, "views/pharmacie/sidebar-view.fxml", "Pharmacie", false);
                            case "medecin" -> {
                                swtichScene.loadScene(password, "views/Medecin/sidebar-view.fxml", "Médecin", false);
                            }
                            case "patient" ->
                                    swtichScene.loadScene(password, "views/patient/sidebar-view.fxml", "Mes RDV", false);
                            case "admin" ->
                                    swtichScene.loadScene(password, "views/Admin/dashboard.fxml", "Admin", false);
                            default -> {
                                loginresult.setText("Rôle inconnu !");
                                loginresult.setStyle("-fx-text-fill: red;");
                            }
                        }
                    } else {
                        loginresult.setText("Username ou password incorrect");
                        loginresult.setStyle("-fx-text-fill: red;");
                    }
                    loading.setVisible(false);
                });

                return null;
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    loginresult.setText("Erreur lors de la connexion.");
                    loginresult.setStyle("-fx-text-fill: red;");
                    loading.setVisible(false);
                });
            }
        };

        new Thread(task).start();
    }

    public void inscription(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/inscription-view.fxml", "Inscription", false);
    }

    @FXML
    private void gotoreset() {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene((Node) null, "views/resetpassword-view.fxml", "Renitialiser", false);
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

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        if (faceDetections.toArray().length > 0) {
            return faceDetections.toArray()[0]; // Return the first detected face
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

    @FXML
    private void loginWithFacialRecognition() {
        loading.setVisible(true);

        Task<User> task = new Task<>() {
            @Override
            protected User call() throws Exception {
                // Capture image from webcam
                Mat capturedImage = captureImage();
                Rect faceRect = detectFace(capturedImage);

                if (faceRect == null) {
                    Platform.runLater(() -> {
                        loginresult.setText("Aucun visage détecté !");
                        loginresult.setStyle("-fx-text-fill: red;");
                        loading.setVisible(false);
                    });
                    return null;
                }

                // Crop the detected face
                Mat detectedFace = new Mat(capturedImage, faceRect);

                // Convert the detected face to a byte array
                MatOfByte matOfByte = new MatOfByte();
                Imgcodecs.imencode(".jpg", detectedFace, matOfByte);
                byte[] capturedFacialData = matOfByte.toArray();

                // Retrieve the user by comparing facial data
                return UserDAO.getUserByFacialData(capturedFacialData);
            }

            @Override
            protected void succeeded() {
                User user = getValue();
                Platform.runLater(() -> {
                    if (user != null) {
                        // Stocker la session utilisateur
                        UserSession.setEmail(user.getEmail());
                        UserSession.setPrenom(user.getPrenom());
                        UserSession.setNom(user.getNom());
                        UserSession.setId(user.getId());
                        UserSession.setRole(user.getRole());
                        UserSession.setPassword(user.getPassword());
                        UserSession.setNumero(user.getNumero());

                        // Sauvegarder l'email dans les préférences
                        Preferences prefs = Preferences.userNodeForPackage(getClass());
                        prefs.put("last_seremail", user.getEmail());

                        // Changer de scène en fonction du rôle
                        SwtichScene swtichScene = new SwtichScene();
                        switch (user.getRole()) {
                            case "pharmacie" ->
                                    swtichScene.loadScene(password, "views/pharmacie/sidebar-view.fxml", "Pharmacie", false);
                            case "medecin" -> {
                                swtichScene.loadScene(password, "views/Medecin/sidebar-view.fxml", "Médecin", false);
                            }
                            case "patient" ->
                                    swtichScene.loadScene(password, "views/patient/sidebar-view.fxml", "Mes RDV", false);
                            case "admin" ->
                                    swtichScene.loadScene(password, "views/Admin/dashboard.fxml", "Admin", false);
                            default -> {
                                loginresult.setText("Rôle inconnu !");
                                loginresult.setStyle("-fx-text-fill: red;");
                            }
                        }
                    } else {
                        loginresult.setText("Visage non reconnu !");
                        loginresult.setStyle("-fx-text-fill: red;");
                    }
                    loading.setVisible(false);
                });
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    loginresult.setText("Erreur lors de la reconnaissance faciale.");
                    loginresult.setStyle("-fx-text-fill: red;");
                    loading.setVisible(false);
                });
            }
        };

        new Thread(task).start();
    }
}