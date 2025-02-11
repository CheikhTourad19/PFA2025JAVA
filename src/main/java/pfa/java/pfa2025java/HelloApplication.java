package pfa.java.pfa2025java;

import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/pharmacie/accueil-view.fxml"));
        Parent root = loader.load();
        double y = root.prefHeight(-1);
        double x = root.prefWidth(-1);
        Scene scene = new Scene(root, x, y);
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.sizeToScene();
        SwtichScene.loadimage(stage);
        stage.show();
        HelloApplication.closeAlert(stage);


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