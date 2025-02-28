package pfa.java.pfa2025java.constant;

public  class MessageRequet {
    public static  String  saveMsg= "INSERT INTO messages (sender_id, receiver_id, content, sent_at, vu) VALUES (?, ?, ?, ?, ?)";
    public static  String  getMsgBetweenUsers="SELECT *, (vu IS FALSE AND sender_id != ?) AS is_unread FROM messages " +
            "WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) " +
            "ORDER BY sent_at ASC";
    public static final String getUsersWithLastMsg = "SELECT u.id , u.prenom, m.content, m.sent_at, m.vu, m.sender_id " +
            "FROM user u " +
            "JOIN messages m ON (u.id = m.sender_id OR u.id = m.receiver_id) " +
            "WHERE (m.sender_id = ? OR m.receiver_id = ?) AND u.id != ? " +
            "AND m.id = ( " +
            "    SELECT MAX(id) " +
            "    FROM messages " +
            "    WHERE (sender_id = u.id AND receiver_id = ?) OR (sender_id = ? AND receiver_id = u.id) " +
            ") " +
            "ORDER BY m.sent_at DESC";
    public static  String  search_Users= "SELECT u.id , u.prenom, m.content, m.sent_at, m.vu, m.sender_id " +
            "FROM user u " +
            "LEFT JOIN messages m ON (u.id = m.sender_id OR u.id = m.receiver_id) " +
            "WHERE u.username LIKE ? AND u.id != ? " +
            "GROUP BY u.id " +
            "ORDER BY m.sent_at DESC";
    public static  String  getNewMsg= "SELECT * FROM messages " +
            "WHERE ((sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)) " +
            "AND sent_at > ?";
    public  static  String markMsgAsSeen="UPDATE messages SET vu = true, seen_at = NOW() " +
            "WHERE sender_id = ? AND receiver_id = ? AND vu = false";



}