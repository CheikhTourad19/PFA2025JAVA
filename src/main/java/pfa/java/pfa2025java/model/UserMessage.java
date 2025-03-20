package pfa.java.pfa2025java.model;


import java.time.Duration;
import java.time.LocalDateTime;

public class UserMessage {
    private int userId;
    private String fullName;
    private String lastMessage;
    private LocalDateTime sentAt;
    private String timeAgo;
    private boolean isSeen;
    private int senderId;

    public UserMessage(int userId, String fullName, String lastMessage,
                       LocalDateTime sentAt, boolean isSeen, int senderId) {
        this.userId = userId;
        this.fullName = fullName;
        this.lastMessage = lastMessage;
        this.sentAt = sentAt;
        this.isSeen = isSeen;
        this.timeAgo = calculateTimeAgo(sentAt);
        this.senderId = senderId;


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

    public boolean isSeen() { return isSeen; }
    public void setSeen(boolean seen) { isSeen = seen; }
    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }

    @Override
    public String toString() {
        return fullName + ": " + lastMessage + " (" + timeAgo + ")";
    }
    public String getLastMessage() { return lastMessage; }
    public LocalDateTime getSentAt() { return sentAt; }
    public String getTimeAgo() { return timeAgo; }
    public int getSenderId() { return senderId; }




}


