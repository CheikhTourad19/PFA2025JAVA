package pfa.java.pfa2025java;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/hello-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, root.prefWidth(-1), root.prefHeight(-1));
        stage.setTitle("Accueil");
        stage.setScene(scene);
        stage.show();
        HelloApplication.closeAlert(stage);
        UserSession.setEmail("test");
        UserSession.setPassword("test");
        UserSession.setNom("test");
        UserSession.setPrenom("test");
        UserSession.setRole("admin");

    }
    public static void closeAlert(Stage stage) throws IOException {
        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setHeaderText("Quitter");
            alert.setContentText("Voulez vous Quitter");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                event.consume();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}