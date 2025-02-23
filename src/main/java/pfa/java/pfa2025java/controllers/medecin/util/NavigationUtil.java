package pfa.java.pfa2025java.controllers.medecin.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class NavigationUtil {
    private static final String VIEWS__PATH = "/pfa/java/pfa2025java/views/medecin/";

    public static void navigateTo(AnchorPane container, String viewName) {
        try {
            String fullPath = VIEWS__PATH + viewName + "-view.fxml";
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fullPath));

            Parent view = loader.load();


            container.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Erreur de navigation vers " + viewName + " : " + e.getMessage());
            e.printStackTrace();
        }
    }
}