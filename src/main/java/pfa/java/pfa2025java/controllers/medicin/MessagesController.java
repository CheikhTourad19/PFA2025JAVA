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
import pfa.java.pfa2025java.model.UserMessage;

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
    private boolean userScrolledUp = false;
    @FXML
    private ListView<UserMessage> userList;
    public void initialize() {
        try {
            setupNetworkConnection();
            loadPreviousMessages();
            startMessageUpdater();
            startMessageListener();
            loadUserList();
            userList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    int selectedUserId = newValue.getUserId(); // استرجاع userId الخاص بالمستخدم المحدد
                    System.out.println("Selected User ID: " + selectedUserId); // طباعة userId (لأغراض الاختبار)
                    loadChatWithUser(selectedUserId); // تحميل الرسائل مع المستخدم المحدد
                }
            });
            scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.doubleValue() < 1.0) {
                    userScrolledUp = true;
                } else {
                    userScrolledUp = false;
                }
            });
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
            if (scrollPane != null && !userScrolledUp) {
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
    private void loadUserList() {
        try {
            List<UserMessage> userMessages = messageDao.getUsersWithLastMessage(userId);

            userList.getItems().setAll(userMessages);

            userList.setCellFactory(param -> new ListCell<UserMessage>() {
                @Override
                protected void updateItem(UserMessage userMessage, boolean empty) {
                    super.updateItem(userMessage, empty);

                    if (empty || userMessage == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        HBox hbox = new HBox(10); // تباعد بين العناصر
                        hbox.setAlignment(Pos.CENTER_LEFT);

                        Label usernameLabel = new Label(userMessage.getUsername());
                        usernameLabel.setStyle("-fx-font-weight: bold;");

                        Label lastMessageLabel = new Label(userMessage.getLastMessage());
                        lastMessageLabel.setStyle("-fx-text-fill: gray;");

                        Label sentAtLabel = new Label(userMessage.getSentAt().toString());
                        sentAtLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 10;");


                        hbox.getChildren().addAll(usernameLabel, lastMessageLabel, sentAtLabel);


                        setGraphic(hbox);
                    }
                }
            });
        } catch (SQLException e) {
            showError("Failed to load user list: " + e.getMessage());
        }
    }
    private void loadChatWithUser(int otherUserId) {
        try {
            chatBox.getChildren().clear();
            List<Message> messages = messageDao.getMessagesBetweenUsers(userId, otherUserId);
            for (Message message : messages) {
                addMessageToUI(message.getSenderId(), message.getContent());
            }
            scrollToBottom();
        } catch (SQLException e) {
            showError("Failed to load messages: " + e.getMessage());
        }
    }


}