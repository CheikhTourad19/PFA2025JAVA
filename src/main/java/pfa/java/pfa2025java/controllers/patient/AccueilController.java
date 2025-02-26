package pfa.java.pfa2025java.controllers.patient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.RendezVous;
import pfa.java.pfa2025java.dao.RendezVousDAO;

import java.sql.SQLException;
import java.util.List;

public class AccueilController {

    @FXML
    private TableView<RendezVous> TVRDV;

    @FXML
    private TableColumn<RendezVous, String> TcMedecin;

    @FXML
    private TableColumn<RendezVous, String> TcDate;

    @FXML
    private TableColumn<RendezVous, String> TcStatus;

    @FXML
    private Text nomUtil;


    @FXML
    public void initialize() {
        nomUtil.setText(UserSession.getPrenom()+" "+UserSession.getNom());
        TcMedecin.setCellValueFactory(new PropertyValueFactory<>("medecinNom")); // Assuming medecinId refers to doctor's name
        TcDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        TcStatus.setCellValueFactory(new PropertyValueFactory<>("statut"));

        TcStatus.setCellFactory(column -> {
            return new TableCell<RendezVous, String>() {
                @Override
                protected void updateItem(String statut, boolean empty) {
                    super.updateItem(statut, empty);
                    if (empty || statut == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(statut);
                        // Change color based on status
                        if ("confirme".equals(statut)) {
                            setTextFill(javafx.scene.paint.Color.WHITE);
                            setStyle("-fx-background-color: green;"); // Green for "confirme"
                        } else if ("attente".equals(statut)) {
                            setTextFill(javafx.scene.paint.Color.WHITE);
                            setStyle("-fx-background-color: orange;"); // Orange for "attente"
                        } else if ("annule".equals(statut)) {
                            setTextFill(javafx.scene.paint.Color.WHITE);
                            setStyle("-fx-background-color: #ff3f3f;"); // Red for "annule"
                        } else {
                            setTextFill(javafx.scene.paint.Color.BLACK); // Default color (black) for any other status
                        }
                    }
                }
            };
        });

        try {
            loadRDVs();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly
        }
    }

    private void loadRDVs() throws SQLException {
        List<RendezVous> rdvList = RendezVousDAO.getRDVByPatientId(UserSession.getId()); // Fetch RDVs
        ObservableList<RendezVous> observableList = FXCollections.observableArrayList(rdvList);
        TVRDV.setItems(observableList); //
    }

    public void consulterProfil(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/profile-view.fxml", "Profil", false);
    }


    public void consulterOrdonnances(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/ordonnance-view.fxml", "Ordonnances", false);
    }

    public void demandeRendezVous(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/demanderdv-view.fxml", "Rendez-vous", false);
    }

    public void consulterPharmacies(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/patient/pharmacie-view.fxml", "Pharmacies", false);
    }

    public void logout(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/hello-view.fxml", "Login", false);
        UserSession.logout();
    }

    public void chat(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/chatbotgheith-view.fxml", "Login", false);
    }
}

