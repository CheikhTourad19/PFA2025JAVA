package pfa.java.pfa2025java.model;

import pfa.java.pfa2025java.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static final Connection connection;

    static {
        try {
            connection = DBconnection.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static User getUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("numero")// Le mot de passe est stocké sous forme hashée
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean login(String email, String password) {
        User user = getUserByEmail(email);
        return user != null && PasswordUtils.checkPassword(password, user.getPassword());
    }

    public static boolean registerUser(String nom, String email, String password, String prenom, String numero) {
        String hashedPassword = PasswordUtils.hashPassword(password);  // Hashage du mot de passe
        String sql = "INSERT INTO user (nom, email, password,prenom,numero) VALUES (?, ?, ?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, prenom);
            stmt.setString(5, numero);

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean changePassword(String newPassword) throws SQLException {
        String sql = "UPDATE user SET password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, PasswordUtils.hashPassword(newPassword));
            stmt.setInt(2, UserSession.getId());
            return stmt.executeUpdate() > 0;
        }

    }
    public static boolean updateEmail(String newEmail) throws SQLException {
        String sql = "UPDATE user SET email = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, newEmail);
            stmt.setInt(2, UserSession.getId());
            return stmt.executeUpdate() > 0;
        }
    }
}
