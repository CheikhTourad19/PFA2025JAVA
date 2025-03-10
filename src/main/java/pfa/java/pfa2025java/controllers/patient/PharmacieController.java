package pfa.java.pfa2025java.controllers.patient;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import pfa.java.pfa2025java.dao.PharmacieDAO;
import pfa.java.pfa2025java.model.Pharmacie;

import java.sql.SQLException;

public class PharmacieController {

    @FXML
    private TableView<Pharmacie> pharmacieTable;
    @FXML
    private TableColumn<Pharmacie, String> nomColumn;
    @FXML
    private TableColumn<Pharmacie, String> adresseColumn;
    @FXML
    private TableColumn<Pharmacie, String> emailColumn;
    @FXML
    private TableColumn<Pharmacie, String> numeroColumn;
    @FXML
    private TextField FilterField;

    private ObservableList<Pharmacie> pharmacieList = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom() + " " + cellData.getValue().getPrenom()));
        adresseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse().toString()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        numeroColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumero()));

        pharmacieList.addAll(PharmacieDAO.getPharmaciesWithAdresse());
        pharmacieTable.setItems(pharmacieList);
    }

    @FXML
    private void onFilter() {
        String filter = FilterField.getText().toLowerCase();
        if (filter.isEmpty()) {
            pharmacieTable.setItems(pharmacieList);
        } else {
            ObservableList<Pharmacie> filteredList = pharmacieList.filtered(pharmacie ->
                    pharmacie.getAdresse().toString().toLowerCase().contains(filter)
            );
            pharmacieTable.setItems(filteredList);
        }
    }
}
