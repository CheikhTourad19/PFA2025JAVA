package pfa.java.pfa2025java.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedecinDAO {

    private static final Connection connection;

    static {
        try {
            connection = DBconnection.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Medecin> getAllMedecin() throws SQLException {
        List<Medecin> medecins = new ArrayList<>();
        String sql = "SELECT u.id, u.nom, u.prenom, u.email, u.password, u.role, u.numero, m.service " +
                "FROM user AS u " +
                "JOIN medecin AS m ON u.id = m.id";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                medecins.add(new Medecin(
                        rs.getInt("id"),         // ✅ Changed from "m.id" to "id"
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("numero"),
                        rs.getString("service")
                ));
            }
        }
        return medecins;
    }

}
