package pfa.java.pfa2025java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResetPasswordDAO {
    private static final Connection connection;

    static {
        try {
            connection = DBconnection.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean createDemande(String email, String code) {
        String query = "INSERT INTO renitialiserpassword (user_email, code) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, code);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la demande de réinitialisation : " + e.getMessage());
        }
        return false;
    }


    public static boolean checkCode(String code, String email) {
        String sql = "SELECT * FROM renitialiserpassword WHERE user_email=? AND code=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, code);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Renvoie true si un enregistrement est trouvé
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du code : " + e.getMessage());
        }
        return false;
    }

}
