package pfa.java.pfa2025java.controllers.pharmacie;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.model.Medicament;
import pfa.java.pfa2025java.model.OrdonnanceDAO;
import pfa.java.pfa2025java.model.OrdonnanceDetails;

public class OrdonnanceController {


    public TableColumn<Medicament, Integer> dispoColumn;
    public Text total;
    public TableColumn<Medicament, Integer> prixColumn;
    @FXML
    private TableView<Medicament> ordonnannceTable;
    @FXML
    private TableColumn<Medicament, String> Medicamentcolumn;
    @FXML
    private TableColumn<Medicament, Integer> quantiteColumn;
    @FXML
    private TableColumn<Medicament, String> instructionColumn;
    @FXML
    private Text medecinname;
    @FXML
    private Text patientname;
    @FXML
    private TextField code;

    private final ObservableList<Medicament> medicamentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Medicamentcolumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        dispoColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        quantiteColumn.setCellValueFactory(cellData -> cellData.getValue().quantiteProperty().asObject());
        instructionColumn.setCellValueFactory(cellData -> cellData.getValue().instructionProperty());
        prixColumn.setCellValueFactory(cellData -> cellData.getValue().prixProperty().asObject());
        ordonnannceTable.setItems(medicamentList);
    }

    public void gotostock(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/pharmacie/stock-view.fxml", "Stock", false);
    }

    public void gotoaccueil(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/pharmacie/accueil-view.fxml", "Accueil", false);
    }

    public void logout(ActionEvent actionEvent) {
        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(actionEvent, "views/hello-view.fxml", "Login", false);
    }

    public void search() {
        double somme = 0;
        int code = Integer.parseInt(this.code.getText());
        OrdonnanceDetails ordonnanceDetails = OrdonnanceDAO.getOrdonnanceDetailsById(code);
        if (ordonnanceDetails != null) {
            medecinname.setText("Medecin : " + ordonnanceDetails.getMedecinNom());
            patientname.setText("Patient :" + ordonnanceDetails.getPatientNom());
            for (Medicament medicament : ordonnanceDetails.getMedicaments()) {
                somme = somme + (medicament.getPrix() * medicament.getQuantite());
            }
            // Remplir la table avec les médicaments
            medicamentList.clear();
            medicamentList.addAll(ordonnanceDetails.getMedicaments());
            ordonnannceTable.setItems(medicamentList);
            total.setText("Total : " + somme);
        }
    }
}
