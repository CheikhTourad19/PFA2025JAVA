package pfa.java.pfa2025java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.User;
import pfa.java.pfa2025java.model.UserDAO;




public class HelloController {
    @FXML
    private Text loginresult;
    @FXML
    private TextField username;
    @FXML
    private TextField password;



    @FXML
    public void login(ActionEvent event) {

        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            loginresult.setText("Username ou password vide");
            loginresult.setStyle("-fx-text-fill: red;");
        }else {
            if (UserDAO.login(username.getText(), password.getText())) {
                SwtichScene swtichScene = new SwtichScene();
                User user = UserDAO.getUserByEmail(username.getText());
                assert user != null;
                UserSession.setEmail(user.getEmail());
                UserSession.setPrenom(user.getPrenom());
                UserSession.setNom(user.getNom());
                UserSession.setId(user.getId());
                UserSession.setRole(user.getRole());
                UserSession.setPassword(user.getPassword());
                switch (user.getRole()) {
                    case "pharmacie" ->
                            swtichScene.loadScene(event, "views/pharmacie/accueil-view.fxml", "Pharmacie", false);
                    case "medecin" -> {

                    }
                    case "patient" ->
                            swtichScene.loadScene(loginresult, "views/patient/accueil-view.fxml", "Accueil", false);
                    case "admin" -> swtichScene.loadScene(event, "views/Admin/dashboard.fxml", "Accueil", false);
                    case "infermier" -> {
                    }
                }
            } else {
                loginresult.setText("Username ou password incorrect");
                loginresult.setStyle("-fx-text-fill: red;");
            }
        }
    }


    public void inscription(ActionEvent actionEvent) {

        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/inscription-view.fxml", "Inscription", false);

    }

}