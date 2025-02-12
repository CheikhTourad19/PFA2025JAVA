package pfa.java.pfa2025java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.User;
import pfa.java.pfa2025java.model.UserDAO;

import java.io.IOException;



public class HelloController {
    @FXML
    private Text loginresult;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button login;
    @FXML
    private Button Register;


    @FXML
    public void login(ActionEvent event) {

        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            loginresult.setText("Username ou password vide");
            loginresult.setStyle("-fx-text-fill: red;");
        }else {
            if (UserDAO.login(username.getText(), password.getText())) {
                SwtichScene swtichScene = new SwtichScene();
                User user = UserDAO.getUserByEmail(username.getText());
                UserSession.setEmail(user.getEmail());
                UserSession.setPrenom(user.getPrenom());
                UserSession.setNom(user.getNom());
                UserSession.setId(user.getId());
                UserSession.setRole(user.getRole());
                if (user.getRole().equals("pharmacie")) {
                    swtichScene.loadScene(event, "views/pharmacie/accueil-view.fxml", "Pharmacie", false);
                } else if (user.getRole().equals("medecin")) {

                } else if (user.getRole().equals("patient")) {
                    swtichScene.loadScene(loginresult,"views/patient/accueil-view.fxml", "Accueil", false);
                } else if (user.getRole().equals("admin")) {
                    swtichScene.loadScene(event, "views/Admin/dashboard.fxml", "Accueil", false);
                } else if (user.getRole().equals("infermier")) {

                }
            } else {
                loginresult.setText("Username ou password incorrect");
                loginresult.setStyle("-fx-text-fill: red;");
            }
        }
    }


    public void inscription(ActionEvent actionEvent) throws IOException {

        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/inscription-view.fxml", "Inscription", false);

    }

}