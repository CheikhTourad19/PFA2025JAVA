package pfa.java.pfa2025java.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.User;
import pfa.java.pfa2025java.model.UserDAO;

import java.util.prefs.Preferences;


public class HelloController {
    public ProgressIndicator loading;
    @FXML
    private Text loginresult;
    @FXML
    private TextField username;
    @FXML
    private TextField password;


    @FXML
    public void initialize() {
        loading.setVisible(false);
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        String last_seremail = prefs.get("last_seremail", "");
        if (!last_seremail.isEmpty()) {
            username.setText(last_seremail);
        }
    }

    @FXML
    public void login(ActionEvent event) {
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
                            case "pharmacie" -> swtichScene.loadScene(event, "views/pharmacie/accueil-view.fxml", "Pharmacie", false);
                            case "medecin" -> {

                                swtichScene.loadScene(event, "views/medicin/sidebar-view.fxml", "Médecin", false);
                            }
                            case "patient" ->
                                    swtichScene.loadScene(event, "views/patient/mesRDV.fxml", "Mes RDV", false);
                            case "admin" -> swtichScene.loadScene(event, "views/Admin/dashboard.fxml", "Admin", false);
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

}