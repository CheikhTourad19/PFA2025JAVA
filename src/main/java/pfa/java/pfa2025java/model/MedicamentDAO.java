package pfa.java.pfa2025java.model;

import pfa.java.pfa2025java.UserSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentDAO {
    private static Connection connection;

    static {
        try {
            connection = DBconnection.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Medicament getMedicamentByNom(String nom) {
        String sql = "SELECT * FROM medicament WHERE nom = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Medicament(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getInt("prix"),
                        0
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Medicament getMedicamentById(int id) {
        String sql = "SELECT * FROM medicament WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Medicament(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getInt("prix"),
                        0
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<Medicament> getMedicamentsByPharmacie() {
        List<Medicament> medicaments = new ArrayList<>();
        String sql = "SELECT m.id, m.nom, m.description, pm.quantite , m.prix " +
                "FROM medicament m " +
                "JOIN stock pm ON m.id = pm.medicament_id " +
                "WHERE pm.pharmacie_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, UserSession.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                medicaments.add(new Medicament(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getInt("prix"),
                        rs.getInt("quantite")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicaments;
    }


    public static boolean ajouterStock(int idMedicament, int increment) {
        String sql = "UPDATE stock SET quantite = quantite + ? WHERE pharmacie_id = ? AND medicament_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, increment);
            stmt.setInt(2, UserSession.getId());
            stmt.setInt(3, idMedicament);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // Retourne true si la mise à jour a réussi
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addMedicamentWithStock(String nom, String description, String prix) {
        String sqlMedicament = "INSERT INTO medicament (nom, description,prix) VALUES (?, ?,?)";
        String sqlStock = "INSERT INTO stock (pharmacie_id, medicament_id, quantite) VALUES (?, ?, 0)";

        try (PreparedStatement stmtMedicament = connection.prepareStatement(sqlMedicament, Statement.RETURN_GENERATED_KEYS)) {
            stmtMedicament.setString(1, nom);
            stmtMedicament.setString(2, description);
            stmtMedicament.setInt(3, Integer.parseInt(prix));
            int rowsInserted = stmtMedicament.executeUpdate();

            if (rowsInserted > 0) {
                // Récupérer l'ID du médicament nouvellement créé
                ResultSet generatedKeys = stmtMedicament.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idMedicament = generatedKeys.getInt(1);

                    // Ajouter le stock initialisé à 0 pour la pharmacie donnée
                    try (PreparedStatement stmtStock = connection.prepareStatement(sqlStock)) {
                        stmtStock.setInt(1, UserSession.getId());
                        stmtStock.setInt(2, idMedicament);
                        stmtStock.executeUpdate();
                    }

                    return true; // Succès
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Échec
    }
}

