package pfa.java.pfa2025java;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class HelloController {
    @FXML
    private Label welcomeText;
    private int num=0;
    public Image image;
    @FXML
    protected void onHelloButtonClick() {
        num++;
        welcomeText.setText("Welcome to JavaFX Application!"+num);
    }
    @FXML
    protected void onclikini(){
        welcomeText.setText("Baraaaaa neyek bataaaaaaard");

    }
}