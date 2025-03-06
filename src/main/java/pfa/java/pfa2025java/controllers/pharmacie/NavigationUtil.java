package pfa.java.pfa2025java.controllers.pharmacie;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


import java.io.IOException;

public class NavigationUtil {
    private static final String VIEWS__PATH = "/pfa/java/pfa2025java/views/pharmacie/";

    public static void navigateTo(VBox container, String viewName, SidebarController sidebarController) {
        try {
            String fullPath = VIEWS__PATH + viewName + "-view.fxml";
            FXMLLoader loader = new FXMLLoader(pfa.java.pfa2025java.controllers.pharmacie.NavigationUtil.class.getResource(fullPath));

            Parent view = loader.load();
            Object controller = loader.getController();

            container.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Erreur de navigation vers " + viewName + " : " + e.getMessage());
            e.printStackTrace();
        }
    }

}
