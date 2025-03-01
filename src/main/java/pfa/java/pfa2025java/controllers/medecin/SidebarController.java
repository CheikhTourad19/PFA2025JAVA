package pfa.java.pfa2025java.controllers.medecin;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.util.Duration;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.controllers.medecin.util.NavigationUtil;
import pfa.java.pfa2025java.dao.MessageDAO;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class SidebarController {


    public Label ordonnanceLabel;
    public ImageView ordonnanceImage;
    @FXML
    private AnchorPane mainContent;
    @FXML
    private AnchorPane drawer;
    @FXML
    private ImageView Toggle_sidebar;
    @FXML
    private ImageView dashboard, tasks, messages, appointments, settings, logout, profilImage;
    @FXML
    private Label dashboardLabel, tasksLabel, messagesLabel, appointmentsLabel, settingsLabel, logoutLabel, profilLabel;
    @FXML private Label countMsg;

    private boolean isCollapsed = false;

    @FXML
    private void initialize() {
        initializeComponents();
        goToDashboard();

    }


    private void initializeComponents() {
        setImagePositions(12.0);
        updateLabelsVisibility(!isCollapsed);
        startUnreadCountUpdater();
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
        animateImage(timeline, profilImage, 31.0, targetWidth);
        animateImage(timeline, ordonnanceImage, 31.0, targetWidth);
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
                appointmentsLabel, settingsLabel, logoutLabel, profilLabel, ordonnanceLabel
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
        profilImage.setLayoutX(position);
        ordonnanceImage.setLayoutX(position);
    }


    @FXML


    private void goToMessages() {
        NavigationUtil.navigateTo(mainContent, "message",this);

    }

    @FXML
    private void goToDashboard() {
        NavigationUtil.navigateTo(mainContent, "dashboard",this);
    }
    @FXML
    private void goToTask() {
        NavigationUtil.navigateTo(mainContent, "task",this);
    }

    @FXML
    private void goToProfil() {
        NavigationUtil.navigateTo(mainContent, "profil",this);
    }

    @FXML
    private void goToOrdonnance() {
        NavigationUtil.navigateTo(mainContent, "ordonnance",this);
    }
    @FXML
    private void logout() {
        UserSession.logout();

        SwtichScene swtichScene = new SwtichScene();
        swtichScene.loadScene(logoutLabel,"views/hello-view.fxml","login",false);
    }

    @FXML
    private void goToRDV() {
        NavigationUtil.navigateTo(mainContent, "appointment",this);
    }

    private void startUnreadCountUpdater() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), e -> updateUnreadCount())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    protected void updateUnreadCount() {
        try {
            int unreadCount = new MessageDAO().getUnreadCount(UserSession.getId());
            Platform.runLater(() -> {
                if (unreadCount > 0) {
                    countMsg.setText(String.valueOf(unreadCount));
                    countMsg.setVisible(true);
                } else {
                    countMsg.setVisible(false);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}

