package pfa.java.pfa2025java.controllers.patient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.dao.DBconnection; // Import the DBconnection class
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FeedbackController {

    @FXML private ToggleButton star1, star2, star3, star4, star5;
    @FXML private TextArea commentField;
    @FXML private Button submitButton;

    private int rating = 0;

    @FXML
    public void initialize() {
        ToggleButton[] stars = {star1, star2, star3, star4, star5};

        for (int i = 0; i < stars.length; i++) {
            int starValue = i + 1;
            stars[i].setOnAction(event -> updateStars(starValue, stars));
        }

        submitButton.setOnAction(event -> submitFeedback());
    }

    private void updateStars(int selectedRating, ToggleButton[] stars) {
        rating = selectedRating;
        for (int i = 0; i < stars.length; i++) {
            stars[i].setSelected(i < selectedRating);
        }
    }

    private void submitFeedback() {
        String comment = commentField.getText().trim();
        if (rating == 0 || comment.isEmpty()) {
            showAlert("Error", "Please select a rating and enter a comment.");
            return;
        }

        String query = "INSERT INTO feedback (rating, comment) VALUES (?, ?)";

        try (Connection conn = DBconnection.connect(); // Use DBconnection class
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, rating);
            pstmt.setString(2, comment);
            pstmt.executeUpdate();
            showAlert("Success", "Feedback submitted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to submit feedback.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

