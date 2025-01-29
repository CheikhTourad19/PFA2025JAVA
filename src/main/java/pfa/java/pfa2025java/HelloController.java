package pfa.java.pfa2025java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloController {
    @FXML
    private Label welcomeText;
    private int num=0;
    @FXML
    private Button buttonClose;

    @FXML
    protected void onHelloButtonClick() {
        num++;
        welcomeText.setText("Welcome to JavaFX Application!"+num);
    }
    @FXML
    protected void onclikini() throws IOException {
        welcomeText.setText("Baraaaaa neyek bataaaaaaard");
    }

    @FXML
    protected void buttonClose(ActionEvent event) {
        System.exit(0);
    }
}