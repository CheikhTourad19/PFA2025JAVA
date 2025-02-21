package pfa.java.pfa2025java.controllers.medicin;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.util.Duration;
import pfa.java.pfa2025java.controllers.medicin.util.NavigationUtil;

import java.util.Arrays;
import java.util.List;

public class SidebarController {


    @FXML
    private AnchorPane mainContent;
    @FXML
    private AnchorPane drawer;
    @FXML
    private ImageView Toggle_sidebar;
    @FXML
    private ImageView dashboard, tasks, messages, appointments, settings, logout;
    @FXML
    private Label dashboardLabel, tasksLabel, messagesLabel, appointmentsLabel, settingsLabel, logoutLabel;

    private boolean isCollapsed = false;

    @FXML
    private void initialize() {
        initializeComponents();
        goToDashboard();

    }


    private void initializeComponents() {
        // Position initiale des icônes
        setImagePositions(12.0);
        // État initial des labels
        updateLabelsVisibility(!isCollapsed);
    }

    @FXML
    private void toggleSidebar() {
        isCollapsed = !isCollapsed;
        updateDrawerStyle();
        animateComponents();
    }

    private void updateDrawerStyle() {
        if (isCollapsed) {
            drawer.getStyleClass().add("collapsed");
        } else {
            drawer.getStyleClass().remove("collapsed");
        }
    }

    private void animateComponents() {
        Timeline timeline = new Timeline();
        double targetWidth = isCollapsed ? 50.0 : 150.0;

        // Animation des icônes
        animateImage(timeline, dashboard, 39.0, targetWidth);
        animateImage(timeline, tasks, 31.0, targetWidth);
        animateImage(timeline, messages, 39.0, targetWidth);
        animateImage(timeline, appointments, 39.0, targetWidth);
        animateImage(timeline, settings, 39.0, targetWidth);
        animateImage(timeline, logout, 31.0, targetWidth);
        animateImage(timeline, Toggle_sidebar, Toggle_sidebar.getFitWidth(), targetWidth);

        // Gestion finale des labels
        timeline.setOnFinished(e -> updateLabelsVisibility(!isCollapsed));

        timeline.play();
    }

    private void animateImage(Timeline timeline, ImageView image, double imageWidth, double targetWidth) {
        double targetX = calculateTargetPosition(imageWidth, targetWidth);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(150),
                        new KeyValue(image.layoutXProperty(), targetX)
                )
        );
    }

    private double calculateTargetPosition(double imageWidth, double targetWidth) {
        return targetWidth == 50.0 ?
                (50 - imageWidth) / 2 :
                12.0;
    }

    private void updateLabelsVisibility(boolean visible) {
        List<Label> labels = Arrays.asList(
                dashboardLabel, tasksLabel, messagesLabel,
                appointmentsLabel, settingsLabel, logoutLabel
        );

        labels.forEach(label -> {
            label.setVisible(visible);
            label.setManaged(visible);
        });
    }

    private void setImagePositions(double position) {
        dashboard.setLayoutX(position);
        tasks.setLayoutX(position);
        messages.setLayoutX(position);
        appointments.setLayoutX(position);
        settings.setLayoutX(position);
        logout.setLayoutX(position);
    }


    @FXML


    private void goToMessages() {
        NavigationUtil.navigateTo(mainContent, "message");
    }

    // Exemple pour la page dashboard
    @FXML
    private void goToDashboard() {
        NavigationUtil.navigateTo(mainContent, "dashboard");
    } @FXML
    private void goToTask() {
        NavigationUtil.navigateTo(mainContent, "task");
    }

}

