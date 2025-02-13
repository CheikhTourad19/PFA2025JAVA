package pfa.java.pfa2025java.controllers.medicin.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class NavigationUtil {
    private static final String VIEWS_PATH = "/com/dhasboard/dashboard/";

    public static void navigateTo(AnchorPane container, String viewName) {
        try {
            String fullPath = VIEWS_PATH + viewName + "View.fxml";
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fullPath));

            Parent view = loader.load();


            container.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Erreur de navigation vers " + viewName + " : " + e.getMessage());
            e.printStackTrace();
        }
    }
}