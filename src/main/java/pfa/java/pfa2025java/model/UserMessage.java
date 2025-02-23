package pfa.java.pfa2025java.model;


import java.time.Duration;
import java.time.LocalDateTime;

public class UserMessage {
    private int userId;
    private String username;
    private String lastMessage;
    private LocalDateTime sentAt;
    private String timeAgo;
 public UserMessage() {}
    public UserMessage(int userId, String username, String lastMessage, LocalDateTime sentAt) {
        this.userId = userId;
        this.username = username;
        this.lastMessage = lastMessage;
        this.sentAt = sentAt;
        this.timeAgo = calculateTimeAgo(sentAt);

    }
    private String calculateTimeAgo(LocalDateTime sentAt) {
        Duration duration = Duration.between(sentAt, LocalDateTime.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return "now";
        } else if (seconds < 3600) {
            return (seconds / 60) + "min";
        } else if (seconds < 86400) {
            return (seconds / 3600) + " hour";
        } else {
            return (seconds / 86400) + " day";
        }
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    @Override
    public String toString() {
        return username + ": " + lastMessage + " (" + timeAgo + ")";
    }
}


