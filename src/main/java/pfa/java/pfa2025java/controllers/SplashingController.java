package pfa.java.pfa2025java.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Duration;

public class SplashingController {
    public ProgressIndicator progressIndicator;


    @FXML
    public void initialize() {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5));
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Animation du ProgressIndicator
        progressIndicator.setProgress(-1);
    }
}
