package pfa.java.pfa2025java.dao;

import pfa.java.pfa2025java.constant.MessageRequet;
import pfa.java.pfa2025java.model.Message;
import pfa.java.pfa2025java.model.UserMessage;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    private static final Connection connection;

    static {
        try {
            connection = DBconnection.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveMessage(Message message) throws SQLException {
        String sql = MessageRequet.saveMsg;
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            stmt.setInt(1, message.getSenderId());
            stmt.setInt(2, message.getReceiverId());
            stmt.setString(3, message.getContent());
            stmt.setTimestamp(4, Timestamp.valueOf(message.getSentAt()));
            stmt.setBoolean(5, message.isSeen());

            stmt.executeUpdate();

            connection.commit();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    message.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<Message> getMessagesBetweenUsers(int user1, int user2) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = MessageRequet.getMsgBetweenUsers;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user1);
            stmt.setInt(2, user1);
            stmt.setInt(3, user2);
            stmt.setInt(4, user2);
            stmt.setInt(5, user1);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Message message = new Message(
                            rs.getInt("sender_id"),
                            rs.getInt("receiver_id"),
                            rs.getString("content"),
                            rs.getTimestamp("sent_at").toLocalDateTime()
                    );
                    message.setId(rs.getInt("id"));
                    message.setSeen(rs.getBoolean("vu"));
                    messages.add(message);
                }
            }
        }
        return messages;
    }

    public List<Message> getNewMessages(int user1, int user2, LocalDateTime lastCheck) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = MessageRequet.getNewMsg;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user1);
            stmt.setInt(2, user2);
            stmt.setInt(3, user2);
            stmt.setInt(4, user1);
            stmt.setTimestamp(5, Timestamp.valueOf(lastCheck));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Message message = new Message(
                            rs.getInt("sender_id"),
                            rs.getInt("receiver_id"),
                            rs.getString("content"),
                            rs.getTimestamp("sent_at").toLocalDateTime()
                    );
                    message.setId(rs.getInt("id"));
                    message.setSeen(rs.getBoolean("vu"));
                    messages.add(message);
                }
            }
        }
        return messages;
    }

    public List<UserMessage> getUsersWithLastMessage(int userId) throws SQLException {
        List<UserMessage> userMessages = new ArrayList<>();
        String sql = MessageRequet.getUsersWithLastMsg;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, userId);
            stmt.setInt(4, userId);
            stmt.setInt(5, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UserMessage userMessage = new UserMessage(
                            rs.getInt("id"),
                            rs.getString("fullname"), // Correction ici
                            rs.getString("content"),
                            rs.getTimestamp("sent_at").toLocalDateTime(),
                            rs.getBoolean("vu"),
                            rs.getInt("sender_id")
                    );
                    userMessages.add(userMessage);
                }
            }
        }
        return userMessages;
    }

    public List<UserMessage> searchUsers(int currentUserId, String query) throws SQLException {
        List<UserMessage> userMessages = new ArrayList<>();
        String sql = MessageRequet.search_Users;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchTerm = "%" + query + "%";
            stmt.setString(1, searchTerm);  // Pour prenom LIKE
            stmt.setString(2, searchTerm);  // Pour nom LIKE
            stmt.setInt(3, currentUserId);  // ID à exclure (u.id != ?)

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UserMessage userMessage = new UserMessage(
                            rs.getInt("id"),
                            rs.getString("fullname"), // Récupération du fullname
                            rs.getString("content"),
                            rs.getTimestamp("sent_at") != null ?
                                    rs.getTimestamp("sent_at").toLocalDateTime() : LocalDateTime.now(),
                            rs.getBoolean("vu"),
                            rs.getInt("sender_id")
                    );
                    userMessages.add(userMessage);
                }
            }
        }
        return userMessages;
    }
    public void markMessagesAsSeen(int receiverId, int senderId) throws SQLException {
        String sql = MessageRequet.markMsgAsSeen;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.executeUpdate();
        }
    }
    public int getUnreadCount(int userId) throws SQLException {
        String query = "SELECT COUNT(*) FROM messages WHERE receiver_id = ? AND vu = false";
        try (
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

}