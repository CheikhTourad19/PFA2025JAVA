package pfa.java.pfa2025java;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

import java.io.InputStream;


public class SwtichScene {
    public void loadScene(ActionEvent actionEvent, String fxmlPath, String title, boolean newWindow) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            double y = root.prefHeight(-1);
            double x = root.prefWidth(-1);
            Stage stage;
            if (newWindow) {
                stage = new Stage();
            } else {
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);


            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {

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

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);


            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {

            System.out.println("Erreur de chargement : Impossible de charger la vue " + fxmlPath);

        }
    }





}
