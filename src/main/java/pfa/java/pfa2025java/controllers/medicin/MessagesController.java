package pfa.java.pfa2025java.controllers.medicin;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import pfa.java.pfa2025java.model.Message;
import pfa.java.pfa2025java.model.MessageDAO;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessagesController {
    private Set<String> displayedMessages = new HashSet<>();
    @FXML private TextField messageField;
    @FXML private VBox chatBox;
    @FXML private ScrollPane scrollPane;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final int userId = 2;
    private final  int receiverId = 1;
    private MessageDAO messageDao = new MessageDAO();
    private LocalDateTime sent_at = LocalDateTime.now();

    public void initialize() {
        try {
            setupNetworkConnection();
            loadPreviousMessages();
            startMessageUpdater();
            startMessageListener();
        } catch (IOException e) {
            showError("Connection error: " + e.getMessage());
        }
    }

    private void setupNetworkConnection() throws IOException {
        socket = new Socket("localhost", 5000);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println(userId); // Envoyer l'ID utilisateur au serveur
    }

    private void loadPreviousMessages() {
        try {
            List<Message> messages = messageDao.getMessagesBetweenUsers(userId, receiverId);
            for (Message message : messages) {
                addMessageToUI(message.getSenderId(), message.getContent());
            }
            scrollToBottom();
        } catch (SQLException e) {
            showError("Failed to load messages: " + e.getMessage());
        }
    }

    private void startMessageUpdater() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            checkForNewMessages();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void startMessageListener() {
        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    String[] parts = message.split(":", 2);
                    if (parts.length < 2) continue;

                    int senderId = Integer.parseInt(parts[0]);
                    String content = parts[1];

                    Platform.runLater(() -> {
                        addMessageToUI(senderId, content);
                        scrollToBottom();
                    });
                }
            } catch (IOException e) {
                Platform.runLater(() -> showError("Network error: " + e.getMessage()));
            }
        }).start();
    }

    private void checkForNewMessages() {
        try {
            List<Message> newMessages = messageDao.getNewMessages(userId, receiverId, sent_at);
            for (Message message : newMessages) {
                addMessageToUI(message.getSenderId(), message.getContent());
            }
            sent_at = LocalDateTime.now();
            scrollToBottom();
        } catch (SQLException e) {
            showError("Error checking new messages: " + e.getMessage());
        }
    }

    @FXML
    private void sendMessage(ActionEvent event) {
        String content = messageField.getText().trim();
        if (!content.isEmpty()) {
            Message message = new Message(userId, receiverId, content,sent_at);
            try {
                messageDao.saveMessage(message);
                out.println(receiverId + ":" + content); // Envoyer via le réseau
                addMessageToUI(userId, content);
                messageField.clear();
                scrollToBottom();
            } catch (SQLException e) {
                showError("Failed to send message: " + e.getMessage());
            }
        }
    }

    private void addMessageToUI(int senderId, String content) {
        //refresh mn ghyr message key ysir 3 fois
        String messageKey = senderId + ":" + content+sent_at;
        if (!displayedMessages.contains(messageKey)) {
            HBox messageContainer = new HBox();
            messageContainer.setAlignment(senderId == userId ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
            messageContainer.setPadding(new Insets(5, 10, 5, 10));

            TextFlow textFlow = new TextFlow(new Text(content));
            textFlow.setMaxWidth(300);
            textFlow.setPadding(new Insets(8));
            textFlow.setStyle(senderId == userId ?
                    "-fx-background-color: #0084ff; -fx-background-radius: 15; -fx-text-fill: white;" :
                    "-fx-background-color: #e0e0e0; -fx-background-radius: 15;");

            messageContainer.getChildren().add(textFlow);
            chatBox.getChildren().add(messageContainer);

            displayedMessages.add(messageKey);
        }
    }

    private void scrollToBottom() {
        Platform.runLater(() -> {
            if(scrollPane != null) {
                scrollPane.setVvalue(1.0);
                scrollPane.requestLayout();
            }
        });
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

}