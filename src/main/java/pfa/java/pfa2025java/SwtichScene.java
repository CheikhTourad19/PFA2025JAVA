package pfa.java.pfa2025java;

import javafx.embed.swing.SwingFXUtils;
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
            Scene scene = new Scene(root, x, y);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.sizeToScene();
            stage.resizableProperty().setValue(false);
            loadImage(stage);
            stage.show();
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

            Scene scene = new Scene(root, x, y);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.sizeToScene();
            loadImage(stage);
            stage.show();
        } catch (IOException e) {

            System.out.println("Erreur de chargement : Impossible de charger la vue " + fxmlPath);
        }
    }

    public static void loadImage(Stage stage) {
        String imagePath = "assets/img.png"; // Chemin depuis src/main/resources

        try (InputStream imageStream = SwtichScene.class.getResourceAsStream(imagePath)) {
            if (imageStream == null) {
                System.out.println("⚠ Image non trouvée : " + imagePath);
                return;
            }

            // Charger l'image en mémoire
            Image fxImage = new Image(imageStream);
            stage.getIcons().add(fxImage);

            // Définir l'icône pour la barre des tâches (Windows/Linux) et le Dock (macOS)
            setTaskbarIcon(fxImage);
        } catch (Exception e) {
            System.out.println("⚠ Erreur lors du chargement de l'icône : " + e.getMessage());
        }
    }

    private static void setTaskbarIcon(Image fxImage) {
        if (Taskbar.isTaskbarSupported()) {
            try {
                Taskbar taskbar = Taskbar.getTaskbar();
                java.awt.Image awtImage = SwingFXUtils.fromFXImage(fxImage, null);
                if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE) && awtImage != null) {
                    taskbar.setIconImage(awtImage);
                }
            } catch (Exception e) {
                System.out.println("⚠ Impossible de définir l'icône du Dock/Taskbar : " + e.getMessage());
            }
        }
    }

}
