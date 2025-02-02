package pfa.java.pfa2025java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pfa.java.pfa2025java.HelloApplication;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;

import java.io.IOException;
import java.net.URL;


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
            if (username.getText().equals(UserSession.getEmail()) && password.getText().equals(UserSession.getPassword())) {
                loginresult.setText("Bonjour Mr " + UserSession.getNom());
            } else
                loginresult.setText("Mauvais identifiants");

        }
    }


    public void inscription(ActionEvent actionEvent) throws IOException {

        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/inscription-view.fxml", "Inscription", true);

    }

}