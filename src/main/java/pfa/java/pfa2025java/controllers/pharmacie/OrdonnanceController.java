package pfa.java.pfa2025java.controllers.pharmacie;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    public ProgressIndicator loading;
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
        loading.setVisible(false);
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


        if (this.code.getText().isEmpty()) {
            return;
        }
        int code1 = Integer.parseInt(code.getText());
        loading.setVisible(true); // Afficher le ProgressIndicator

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                double somme = 0;

                OrdonnanceDetails ordonnanceDetails = OrdonnanceDAO.getOrdonnanceDetailsById(code1);

                if (ordonnanceDetails != null) {
                    // Calculer la somme des médicaments
                    for (Medicament medicament : ordonnanceDetails.getMedicaments()) {
                        somme += medicament.getPrix() * medicament.getQuantite();
                    }

                    // Mise à jour de l'interface graphique (doit être dans `Platform.runLater`)
                    final double finalSomme = somme;
                    Platform.runLater(() -> {
                        medecinname.setText("Médecin : Dr " + ordonnanceDetails.getMedecinNom());
                        patientname.setText("Patient : " + ordonnanceDetails.getPatientNom() + " ");

                        // Mettre à jour la table
                        medicamentList.clear();
                        medicamentList.addAll(ordonnanceDetails.getMedicaments());
                        ordonnannceTable.setItems(medicamentList);

                        // Afficher le total
                        total.setText("Total : " + finalSomme);
                    });
                }

                return null;
            }

            @Override
            protected void succeeded() {
                loading.setVisible(false); // Masquer après la fin
            }

            @Override
            protected void failed() {
                loading.setVisible(false); // Masquer même en cas d’erreur
            }
        };

        new Thread(task).start();
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
            contentStream.showText("Ordonnance Médicale Par E-Medical");
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

    public void retirer() {
        boolean success = OrdonnanceDAO.retirerOrdonnance(Integer.parseInt(code.getText()));
        Alert alert;
        if (success) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Ordonnance retirer Avec Succes veiller consulter votre stock");
            alert.setTitle("Succes");
            alert.setHeaderText(null);
            alert.showAndWait();
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Echec de la retirer Ordonnance veiller consulter votre stock");
            alert.setTitle("Echec");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
        medicamentList.clear();
        medecinname.setText("");
        patientname.setText("");
        total.setText("");

    }
}

