package pfa.java.pfa2025java.controllers.medicin;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessagesController {

    public AnchorPane messageRoot;
    public Button sendButton;
    @FXML private ListView<String> userList;
    @FXML private ListView<String> messageList;
    @FXML private TextField messageField;

    private PrintWriter out;
    private Socket socket;
    private String currentUserId;
    private String currentUserRole;
    private ObservableList<String> messages = FXCollections.observableArrayList();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public void initialize(String userId, String role) {
        this.currentUserId = userId;
        this.currentUserRole = role;

        messageList.setItems(messages);
        loadContacts();
        connectToServer();
        setupUserListListener();
    }

    private void loadContacts() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/medical_chat", "root", "")) {
            String query = currentUserRole.equals("MEDECIN") ?
                    "SELECT user_id, nom FROM users WHERE role = 'INFERMIER'" :
                    "SELECT user_id, nom FROM users WHERE role = 'MEDECIN'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            ObservableList<String> contacts = FXCollections.observableArrayList();
            while(rs.next()) {
                contacts.add(rs.getString("user_id") + " - " + rs.getString("nom"));
            }
            userList.setItems(contacts);

        } catch (SQLException e) {
            showError("Erreur de chargement des contacts", e);
        }
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 5555);
                out = new PrintWriter(socket.getOutputStream(), true);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    String finalMessage = message;
                    Platform.runLater(() -> messages.add(formatIncomingMessage(finalMessage)));
                }
            } catch (IOException e) {
                showError("Erreur de connexion au serveur", e);
            }
        }).start();
    }

    private void setupUserListListener() {
        userList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> loadMessageHistory(newValue)
        );
    }

    private void loadMessageHistory(String selectedUser) {
        if (selectedUser == null) return;

        String[] parts = selectedUser.split(" - ");
        String receiverId = parts[0];

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/medical_chat", "root", "")) {
            String query = """
                SELECT m.*, u.nom 
                FROM messages m
                JOIN users u ON m.sender_id = u.user_id
                WHERE (m.sender_id = ? AND m.receiver_id = ?)
                   OR (m.sender_id = ? AND m.receiver_id = ?)
                ORDER BY m.sent_at
                """;

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, currentUserId);
            pstmt.setString(2, receiverId);
            pstmt.setString(3, receiverId);
            pstmt.setString(4, currentUserId);

            ResultSet rs = pstmt.executeQuery();
            messages.clear();
            while(rs.next()) {
                messages.add(formatMessageFromDB(rs));
            }
        } catch (SQLException e) {
            showError("Erreur de chargement de l'historique", e);
        }
    }

    @FXML
    private void handleSendMessage() {
        String selected = userList.getSelectionModel().getSelectedItem();
        String content = messageField.getText().trim();

        if (selected == null || selected.isEmpty()) {
            showAlert("Sélectionnez un contact avant d'envoyer un message");
            return;
        }

        if (content.isEmpty()) {
            showAlert("Le message ne peut pas être vide");
            return;
        }

        String[] parts = selected.split(" - ");
        String receiverId = parts[0];

        sendMessageToServer(receiverId, content);
        messageField.clear();
    }

    private void sendMessageToServer(String receiverId, String content) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        String message = String.format("%s|%s|%s|%s",
                currentUserId,
                receiverId,
                content,
                timestamp
        );

        out.println(message);
        messages.add(formatOutgoingMessage(content, timestamp));
    }

    private String formatMessageFromDB(ResultSet rs) throws SQLException {
        String senderId = rs.getString("sender_id");
        String senderName = senderId.equals(currentUserId) ?
                "Vous" :
                rs.getString("nom");

        return String.format("[%s] %s: %s",
                rs.getTimestamp("sent_at").toLocalDateTime().format(TIME_FORMATTER),
                senderName,
                rs.getString("contenu")
        );
    }

    private String formatIncomingMessage(String rawMessage) {
        String[] parts = rawMessage.split("\\|", 4);
        String senderId = parts[0];
        String content = parts[2];
        String time = parts[3];

        return senderId.equals(currentUserId) ?
                formatOutgoingMessage(content, time) :
                String.format("[%s] %s: %s", time, getContactName(senderId), content);
    }

    private String formatOutgoingMessage(String content, String time) {
        return String.format("[%s] Vous: %s", time, content);
    }

    private String getContactName(String userId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/medical_chat", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT nom FROM users WHERE user_id = ?");
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("nom") : "Inconnu";
        } catch (SQLException e) {
            return "Inconnu";
        }
    }

    private void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void showError(String context, Exception e) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(context);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        });
        e.printStackTrace();
    }
}