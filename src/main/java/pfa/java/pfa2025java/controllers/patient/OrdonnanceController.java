package pfa.java.pfa2025java.controllers.patient;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.Medicament;
import pfa.java.pfa2025java.dao.OrdonnanceDAO;
import pfa.java.pfa2025java.model.OrdonnanceDetails;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class OrdonnanceController {

    public TableView<OrdonnanceDetails> ordonnancesTable;
    public TableColumn <OrdonnanceDetails,String> MedecinColOrd;
    public TableColumn <OrdonnanceDetails,String> dateColOrd;
    public TableView <Medicament> medicamentTable;
    public TableColumn<OrdonnanceDetails,String> mednameCol;
    public TableColumn<Medicament,Integer> quantiteCol;
    public TableColumn<Medicament,String> instruCol;


    private final ObservableList<OrdonnanceDetails> ordonnanceDetailsList = FXCollections.observableArrayList();
    private final ObservableList<Medicament> medicamentList = FXCollections.observableArrayList();
    public ProgressIndicator loading;


    public void initialize() {
        medicamentTable.setVisible(false);
        MedecinColOrd.setCellValueFactory(new PropertyValueFactory<>("medecinNom"));
        dateColOrd.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));

        mednameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        quantiteCol.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        instruCol.setCellValueFactory(new PropertyValueFactory<>("instruction"));

        // Associer les listes aux tables
        ordonnancesTable.setItems(ordonnanceDetailsList);
        medicamentTable.setItems(medicamentList);

        // Ajouter un listener pour charger les médicaments lors de la sélection d'une ordonnance
        ordonnancesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                medicamentTable.setVisible(true);
                medicamentList.setAll(newSelection.getMedicaments()); // Charger les médicaments
            }
        });

        // Charger les données initiales
        loadOrdonnances();

    }
    private void loadOrdonnances() {
        int patientId = UserSession.getId();  // Remplacez par la vraie source de l'ID patient
        loading.setVisible(true);

        Task<List<OrdonnanceDetails>> task = new Task<>() {
            @Override
            protected List<OrdonnanceDetails> call() {
                return OrdonnanceDAO.getOrdonnanceDetailsByPatient(patientId);
            }

            @Override
            protected void succeeded() {
                // Cacher le loader et mettre à jour l'UI avec les données
                Platform.runLater(() -> {
                    ordonnanceDetailsList.setAll(getValue());
                    loading.setVisible(false);
                });
            }

            @Override
            protected void failed() {
                // En cas d'erreur, cacher le loader
                Platform.runLater(() -> loading.setVisible(false));
            }
        };

        // Exécuter la tâche dans un nouveau thread
        new Thread(task).start();

    }

    public void exportToPdf(ActionEvent actionEvent) {
         // Rafraîchir la liste avant l'exportation

        OrdonnanceDetails selectedOrdonnance = ordonnancesTable.getSelectionModel().getSelectedItem();
        if (selectedOrdonnance == null) {
            System.out.println("Aucune ordonnance sélectionnée !");
            return;
        }
        medicamentList.setAll(selectedOrdonnance.getMedicaments());
        int code = selectedOrdonnance.getOrdonnanceId();
        String medecinname = selectedOrdonnance.getMedecinNom();
        String patientname = selectedOrdonnance.getPatientNom();

        int total=0;
        for (Medicament medicament : medicamentList) {
            total+=medicament.getPrix();
        }
        // Ouvrir un dialogue pour enregistrer le fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
        fileChooser.setInitialFileName("ordonnance_" + code + ".pdf");
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
            contentStream.showText(medecinname);
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText(patientname);
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
            contentStream.showText("Total: " + total);
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
