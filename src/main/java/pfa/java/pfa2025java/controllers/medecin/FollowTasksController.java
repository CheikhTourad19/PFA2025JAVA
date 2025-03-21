package pfa.java.pfa2025java.controllers.medecin;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.dao.TaskDAO;
import pfa.java.pfa2025java.model.Task;
import pfa.java.pfa2025java.model.TaskStatus;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FollowTasksController {

    @FXML
    private ListView<Task> tasksListView;
    private final TaskDAO taskDAO = new TaskDAO();
    private final int userId = UserSession.getId();

    @FXML
    public void initialize() {
        if (tasksListView != null) {
            configureListView();
            loadTasks();
            setupAutoRefresh();
        } else {
            System.err.println("Erreur: ListView non injectée !");
        }
    }

    private void configureListView() {
        tasksListView.setCellFactory(lv -> new ListCell<Task>() {
            private final Label titleLabel = new Label();
            private final Label assigneeLabel = new Label();
            private final Label statusLabel = new Label();
            private final Label deadlineLabel = new Label();
            private final VBox mainContainer = new VBox();
            private final HBox headerBox = new HBox();

            {
                setupStyles();
                setupLayout();
            }

            private void setupStyles() {
                titleLabel.getStyleClass().add("task-title");
                assigneeLabel.getStyleClass().add("task-assignee");
                deadlineLabel.getStyleClass().add("task-deadline");
                statusLabel.getStyleClass().add("task-status");
                mainContainer.getStyleClass().add("task-container");
                headerBox.getStyleClass().add("task-header");
            }

            private void setupLayout() {
                headerBox.getChildren().addAll(titleLabel, statusLabel);
                headerBox.setSpacing(10);

                mainContainer.getChildren().addAll(
                        headerBox,
                        assigneeLabel,
                        deadlineLabel
                );
                mainContainer.setSpacing(8);
            }

            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setGraphic(null);
                } else {
                    updateTaskInfo(task);
                    updateStatusStyle(task.getStatus());
                    setGraphic(mainContainer);
                }
            }

            private void updateTaskInfo(Task task) {
                titleLabel.setText(task.getTitle());
                assigneeLabel.setText("Assigné à: " +
                        (task.getAssigneeName() != null ? task.getAssigneeName() : "Non assigné"));
                deadlineLabel.setText("Échéance: " +
                        task.getDeadline().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                statusLabel.setText(task.getStatus().toString());
            }

            private void updateStatusStyle(TaskStatus status) {
                statusLabel.getStyleClass().removeAll(
                        "status-pending",
                        "status-in-progress",
                        "status-completed"
                );

                switch (status) {
                    case PENDING:
                        statusLabel.getStyleClass().add("status-pending");
                        break;
                    case IN_PROGRESS:
                        statusLabel.getStyleClass().add("status-in-progress");
                        break;
                    case COMPLETED:
                        statusLabel.getStyleClass().add("status-completed");
                        break;
                }
            }
        });
    }

    private void loadTasks() {
        try {
            List<Task> tasks = taskDAO.getTasksCreatedByUser(userId);
            tasksListView.getItems().setAll(tasks);
        } catch (SQLException e) {
            showError("Erreur de chargement",
                    "Impossible de charger les tâches: " + e.getMessage());
        }
    }

    private void setupAutoRefresh() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> loadTasks()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}