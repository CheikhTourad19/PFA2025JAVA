package pfa.java.pfa2025java;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class Chatbot extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        showSplashScreen(primaryStage);
    }

    private void showSplashScreen(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/splashing-view.fxml"));
        Parent splashRoot = loader.load();
        Stage splashStage = new Stage();
        splashStage.initStyle(StageStyle.UNDECORATED); // Supprime la barre de titre

        splashStage.setScene(new Scene(splashRoot, splashRoot.prefWidth(-1), splashRoot.prefHeight(-1)));
        splashStage.show();

        // Pause de 2 secondes avant d'afficher la fenêtre principale
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            splashStage.close();
            showMainStage(primaryStage);
        });
        pause.play();
    }

    private void showMainStage(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/chatbot-view.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            SwtichScene.loadImage(stage);
            stage.show();
            stage.setTitle("E-Medical");
            closeAlert(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeAlert(Stage stage) {
        stage.setOnCloseRequest(event -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous quitter ?");
            java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != javafx.scene.control.ButtonType.OK) {
                event.consume();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
