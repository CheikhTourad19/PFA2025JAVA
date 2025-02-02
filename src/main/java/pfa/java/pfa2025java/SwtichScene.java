package pfa.java.pfa2025java;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SwtichScene {
    public void loadScene(ActionEvent actionEvent, String fxmlPath, String title, boolean newWindow) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();


            Stage stage;
            if (newWindow) {
                stage = new Stage(); // Ouvre une nouvelle fenêtre
            } else {
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow(); // Remplace la scène actuelle
            }


            Scene scene = new Scene(root, 500, 580);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur de chargement Impossible de charger la vue : " + fxmlPath);
        }
    }

}
