package pfa.java.pfa2025java.controllers.pharmacie;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.model.Medicament;
import pfa.java.pfa2025java.model.OrdonnanceDAO;
import pfa.java.pfa2025java.model.OrdonnanceDetails;

import java.io.File;
import java.io.IOException;


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
            medecinname.setText("Medecin : Dr " + ordonnanceDetails.getMedecinNom());
            patientname.setText("Patient :" + ordonnanceDetails.getPatientNom() + " ");
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

    public void exportToPdf(ActionEvent actionEvent) {
        if (medicamentList.isEmpty()) {
            System.out.println("Aucune ordonnance à exporter !");
            return;
        }

        // Ouvrir un dialogue pour enregistrer le fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
        fileChooser.setInitialFileName("ordonnance_" + code.getText() + ".pdf");
        File file = fileChooser.showSaveDialog(new Stage());

        if (file == null) {
            return; // Annulation par l'utilisateur
        }

        // Créer un document PDF avec Apache PDFBox
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Ajouter l'en-tête
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.newLineAtOffset(200, 750);
            contentStream.showText("Ordonnance Médicale");
            contentStream.endText();

            // Infos médecin & patient
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText(medecinname.getText());
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText(patientname.getText());
            contentStream.endText();

            // Ajouter un tableau simple
            int yPosition = 650;
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText("Médicament     Quantité     Instructions");
            contentStream.endText();

            // Remplir le tableau avec les médicaments
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            for (Medicament medicament : medicamentList) {
                yPosition -= 15;
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText(medicament.getNom() + "     " + medicament.getQuantite() + "     "
                        + "     " + medicament.getInstruction());
                contentStream.endText();
            }

            // Ajouter total
            yPosition -= 30;
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText("Total: " + total.getText());
            contentStream.endText();

            contentStream.close();

            // Sauvegarder le fichier PDF
            document.save(file);
            document.close();
            System.out.println("PDF généré avec succès : " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

