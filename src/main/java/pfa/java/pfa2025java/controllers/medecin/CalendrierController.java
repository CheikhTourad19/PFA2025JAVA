package pfa.java.pfa2025java.controllers.medecin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import pfa.java.pfa2025java.dao.RendezVousDAO;
import pfa.java.pfa2025java.model.RendezVous;
import pfa.java.pfa2025java.UserSession;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CalendrierController implements Initializable {
    @FXML
    private GridPane calendarGrid;

    @FXML
    private Label monthLabel;

    private int medecinId = UserSession.getId();
    private LocalDate currentDate = LocalDate.now();
    private List<RendezVous> rendezVousConfirmes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            rendezVousConfirmes = RendezVousDAO.getRendezVousConfirmes(medecinId);
            afficherCalendrier(currentDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void afficherCalendrier(LocalDate date) {
        calendarGrid.getChildren().clear(); // Vider le calendrier
        monthLabel.setText(date.getMonth().name() + " " + date.getYear());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM");
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        int monthLength = date.lengthOfMonth();
        int startDay = firstDayOfMonth.getDayOfWeek().getValue() % 7;

        for (int day = 1; day <= monthLength; day++) {
            LocalDate currentDay = date.withDayOfMonth(day);
            VBox dayBox = new VBox(5);
            dayBox.setStyle("-fx-border-color: #ccc; -fx-padding: 10;");

            Label dateLabel = new Label(currentDay.format(formatter));
            dateLabel.setStyle("-fx-font-weight: bold;");
            dayBox.getChildren().add(dateLabel);

            List<RendezVous> dayRendezVous = rendezVousConfirmes.stream()
                    .filter(rdv -> rdv.getDate().startsWith(currentDay.toString()))
                    .collect(Collectors.toList());

            for (RendezVous rdv : dayRendezVous) {
                Label rdvLabel = new Label(rdv.getPatientId() + " - " + rdv.getDate().split(" ")[1]);
                dayBox.getChildren().add(rdvLabel);
            }

            int col = (startDay + day - 1) % 7;
            int row = (startDay + day - 1) / 7;
            calendarGrid.add(dayBox, col, row);
        }
    }

    @FXML
    private void nextMonth() {
        currentDate = currentDate.plusMonths(1);
        afficherCalendrier(currentDate);
    }

    @FXML
    private void previousMonth() {
        currentDate = currentDate.minusMonths(1);
        afficherCalendrier(currentDate);
    }
}
