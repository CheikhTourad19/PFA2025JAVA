package pfa.java.pfa2025java.constant;

public  class MessageRequet {
    public static  String  saveMsg= "INSERT INTO messages (sender_id, receiver_id, content, sent_at) VALUES (?, ?, ?, ?)";
    public static  String  getMsgBetweenUsers= "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY sent_at ASC";
    public static final String getUsersWithLastMsg =
            "SELECT user_id, prenom, content, sent_at, vu " +
                    "FROM (" +
                    "SELECT u.id AS user_id, u.prenom, m.content, m.sent_at, m.vu, " +
                    "ROW_NUMBER() OVER (PARTITION BY CASE WHEN m.sender_id = ? THEN m.receiver_id ELSE m.sender_id END ORDER BY m.sent_at DESC) AS rn " +
                    "FROM user u " +
                    "JOIN messages m ON (u.id = m.sender_id OR u.id = m.receiver_id) " +
                    "WHERE (m.sender_id = ? OR m.receiver_id = ?) AND u.id != ?" +
                    ") AS ranked " +
                    "WHERE rn = 1 " +
                    "ORDER BY sent_at DESC";
    public static  String  search_Users=  "SELECT u.id AS user_id, u.prenom, ANY_VALUE(m.content) AS content, MAX(m.sent_at) AS sent_at, ANY_VALUE(m.vu) AS vu " +
            "FROM user u " +
            "LEFT JOIN messages m ON (u.id = m.sender_id OR u.id = m.receiver_id) " +
            "WHERE (u.prenom LIKE ?) AND u.id != ? " +
            "GROUP BY u.id " +
            "ORDER BY sent_at DESC";
    public static  String  getNewMsg=  "SELECT * FROM messages WHERE ((sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)) AND sent_at > ?";



}