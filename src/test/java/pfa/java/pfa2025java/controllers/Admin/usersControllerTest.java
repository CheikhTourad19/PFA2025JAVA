package package pfa.java.pfa2025java.controllers.Admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.stage.Stage;

public class UsersControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        // Charger votre FXML ici
        Parent root = FXMLLoader.load(getClass().getResource("/pfa/java/pfa2025java/views/Admin/Users.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void testInitialisationMdp() {
        // Exemple de test
        clickOn("#editButton");
        verifyThat("#Alert", hasText("Password Renitialisee"));
    }
}