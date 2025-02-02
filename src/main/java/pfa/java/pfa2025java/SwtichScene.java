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

            double x = root.prefHeight(-1);
            double y = root.prefWidth(-1);
            Stage stage;
            if (newWindow) {
                stage = new Stage();
            } else {
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            }


            Scene scene = new Scene(root, x, y);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.sizeToScene();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur de chargement Impossible de charger la vue : " + fxmlPath);
        }
    }

    public void loadScene(Node node, String fxmlPath, String title, boolean newWindow) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            double x = root.prefWidth(-1);
            double y = root.prefHeight(-1);
            Stage stage;
            if (newWindow || node == null) {
                stage = new Stage();
            } else {
                stage = (Stage) node.getScene().getWindow();
            }

            Scene scene = new Scene(root, x, y);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.sizeToScene();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur de chargement : Impossible de charger la vue " + fxmlPath);
        }
    }


}
