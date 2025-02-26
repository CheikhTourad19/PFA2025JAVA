package pfa.java.pfa2025java.controllers.patient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
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
    private StackPane chatbotContainer;

    @FXML
    private WebView chatbotWebView;

    @FXML
    private TableColumn<RendezVous, String> TcMedecin;

    @FXML
    private TableColumn<RendezVous, String> TcDate;

    @FXML
    private TableColumn<RendezVous, String> TcStatus;

    @FXML
    private Text nomUtil;

    private boolean isChatbotOpen = false; // Fix: Declare the variable

    @FXML
    public void initialize() {
        // Set user name
        nomUtil.setText(UserSession.getPrenom() + " " + UserSession.getNom());

        // Set up table columns
        TcMedecin.setCellValueFactory(new PropertyValueFactory<>("medecinNom"));
        TcDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        TcStatus.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Customize status cell colors
        TcStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String statut, boolean empty) {
                super.updateItem(statut, empty);
                if (empty || statut == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(statut);
                    switch (statut.toLowerCase()) {
                        case "confirme" -> setStyle("-fx-background-color: green; -fx-text-fill: white;");
                        case "attente" -> setStyle("-fx-background-color: orange; -fx-text-fill: white;");
                        case "annule" -> setStyle("-fx-background-color: #ff3f3f; -fx-text-fill: white;");
                        default -> setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });

        // Load appointment data
        try {
            loadRDVs();
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: Replace with proper logging
        }
    }

    private void loadRDVs() throws SQLException {
        List<RendezVous> rdvList = RendezVousDAO.getRDVByPatientId(UserSession.getId());
        ObservableList<RendezVous> observableList = FXCollections.observableArrayList(rdvList);
        TVRDV.setItems(observableList);
    }

    public void consulterProfil(ActionEvent actionEvent) {
        new SwtichScene().loadScene(actionEvent, "views/patient/profile-view.fxml", "Profil", false);
    }

    public void consulterOrdonnances(ActionEvent actionEvent) {
        new SwtichScene().loadScene(actionEvent, "views/patient/ordonnance-view.fxml", "Ordonnances", false);
    }

    public void demandeRendezVous(ActionEvent actionEvent) {
        new SwtichScene().loadScene(actionEvent, "views/patient/demanderdv-view.fxml", "Rendez-vous", false);
    }

    public void consulterPharmacies(ActionEvent actionEvent) {
        new SwtichScene().loadScene(actionEvent, "views/patient/pharmacie-view.fxml", "Pharmacies", false);
    }

    public void logout(ActionEvent actionEvent) {
        new SwtichScene().loadScene(actionEvent, "views/hello-view.fxml", "Login", false);
        UserSession.logout();
    }

    public void chatbot(ActionEvent actionEvent) {
        if (chatbotContainer != null) {
            isChatbotOpen = !isChatbotOpen;
            chatbotContainer.setVisible(isChatbotOpen);

            if (isChatbotOpen && chatbotWebView != null) {
                String chatbotUrl = getClass().getResource("/pfa/java/pfa2025java/views/chatbot.html").toExternalForm();
                if (chatbotUrl != null) {
                    chatbotWebView.getEngine().load(chatbotUrl);
                } else {
                    System.err.println("❌ Chatbot HTML file not found!");
                }
            }
        } else {
            System.err.println("❌ chatbotContainer is NULL! Check FXML bindings.");
        }
    }

    public void toggleChatbot(ActionEvent actionEvent) {chatbotContainer.setVisible(!chatbotContainer.isVisible());

    }
}
