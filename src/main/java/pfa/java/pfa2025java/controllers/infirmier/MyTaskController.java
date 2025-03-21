package pfa.java.pfa2025java.controllers.infirmier;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.dao.TaskDAO;
import pfa.java.pfa2025java.model.Task;
import pfa.java.pfa2025java.model.TaskStatus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyTaskController {

    @FXML
    private ListView<Task> tasksListView;
    private final TaskDAO taskDAO = new TaskDAO();
    private  int userId = UserSession.getId();

    public void initialize() {
        configureListView();
        loadTasks();
        setupAutoRefresh();
    }

    private void configureListView() {
        tasksListView.setCellFactory(lv -> new ListCell<Task>() {
            private final Label creatorLabel = new Label();
            private final Label titleLabel = new Label();
            private final Label deadlineLabel = new Label();
            private final Label descLabel = new Label();
            private final Button completeButton = new Button("Terminer");
            private final HBox topBox = new HBox();
            private final HBox bottomBox = new HBox();
            private final VBox mainContainer = new VBox();

            {
                setupStyles();
                setupLayout();
                setupButtonAction();
            }

            private void setupStyles() {
                completeButton.getStyleClass().add("complete-button");
                creatorLabel.getStyleClass().add("creator-label");
                titleLabel.getStyleClass().add("title-label");
                deadlineLabel.getStyleClass().add("deadline-label");
                descLabel.getStyleClass().add("description-label");
                mainContainer.getStyleClass().add("task-cell");
                topBox.getStyleClass().add("top-box");

                // Styles inline supplémentaires
                titleLabel.setFont(Font.font("System Bold", 14));
                deadlineLabel.setAlignment(Pos.CENTER_RIGHT);
            }

            private void setupLayout() {
                bottomBox.getChildren().add(completeButton);
                bottomBox.setAlignment(Pos.CENTER_RIGHT);

                // Corrected: Add all children once
                HBox deadlineContainer = new HBox(deadlineLabel);
                deadlineContainer.setAlignment(Pos.CENTER_RIGHT);
                HBox.setHgrow(deadlineContainer, Priority.ALWAYS);

                topBox.getChildren().addAll(creatorLabel, titleLabel, deadlineContainer);
                topBox.setSpacing(10);
                topBox.setAlignment(Pos.CENTER_LEFT);

                // Add topBox, descLabel, and bottomBox once
                mainContainer.getChildren().addAll(topBox, descLabel, bottomBox);
                mainContainer.setSpacing(5);
                mainContainer.setPadding(new Insets(12));
            }
            private void setupButtonAction() {
                completeButton.setOnAction(event -> {
                    Task task = getItem();
                    if (task != null) {
                        try {
                            taskDAO.updateTaskStatus(task.getTaskId(), TaskStatus.COMPLETED);
                            loadTasks(); // Rafraîchir la liste
                        } catch (SQLException e) {
                            showError("Erreur", "Échec de la mise à jour : " + e.getMessage());
                        }
                    }
                });
            }
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setGraphic(null);
                } else {
                    updateCellContent(task);
                    setGraphic(mainContainer);
                    updateButtonVisibility(task);
                }
            }
            private void updateButtonVisibility(Task task) {
                completeButton.setVisible(task.getStatus() != TaskStatus.COMPLETED);
            }
            private void updateCellContent(Task task) {
                creatorLabel.setText("Créé par: " + task.getCreatorName());
                titleLabel.setText(task.getTitle());
                descLabel.setText(task.getDescription());
                deadlineLabel.setText("Échéance: " +
                        task.getDeadline().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                updateStatusStyle(task);
            }

            private void updateStatusStyle(Task task) {
                if (task.getDeadline().isBefore(LocalDate.now())) {
                    mainContainer.setStyle("-fx-border-color: #e74c3c;");
                } else {
                    mainContainer.setStyle("-fx-border-color: #e0e0e0;");
                }
            }
        });
    }

    private void loadTasks() {
        List<Task> tasks = taskDAO.getTasksWithCreatorInfo(userId);
        tasksListView.getItems().setAll(tasks);
    }

    private void setupAutoRefresh() {
        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(10), e -> {
            loadTasks();
            tasksListView.refresh();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void handleRefresh() {
        loadTasks();
    }
    private void showError(String title, String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}
