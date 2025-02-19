package pfa.java.pfa2025java.model;

import pfa.java.pfa2025java.UserSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentDAO {
    private static final Connection connection;

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
            System.out.println(e.getMessage());
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
        // Vérification si le médicament existe déjà
        String sqlCheckMedicament = "SELECT id FROM medicament WHERE nom = ?";
        String sqlMedicament = "INSERT INTO medicament (nom, description, prix) VALUES (?, ?, ?)";
        String sqlStock = "INSERT INTO stock (pharmacie_id, medicament_id, quantite) VALUES (?, ?, 1)";
        String sqlUpdateStock = "UPDATE stock SET quantite = 1 WHERE pharmacie_id = ? AND medicament_id = ?";

        try (PreparedStatement stmtCheckMedicament = connection.prepareStatement(sqlCheckMedicament)) {
            stmtCheckMedicament.setString(1, nom);
            ResultSet rs = stmtCheckMedicament.executeQuery();

            if (rs.next()) {
                // Si le médicament existe, récupérer son ID
                int idMedicament = rs.getInt("id");

                // Vérifier si le stock existe déjà pour ce médicament
                try (PreparedStatement stmtStockCheck = connection.prepareStatement("SELECT quantite FROM stock WHERE pharmacie_id = ? AND medicament_id = ?")) {
                    stmtStockCheck.setInt(1, UserSession.getId());
                    stmtStockCheck.setInt(2, idMedicament);
                    ResultSet stockResult = stmtStockCheck.executeQuery();

                    if (!stockResult.next()) {
                        // Si le stock n'existe pas, initialiser à 1
                        try (PreparedStatement stmtAddStock = connection.prepareStatement(sqlStock)) {
                            stmtAddStock.setInt(1, UserSession.getId());
                            stmtAddStock.setInt(2, idMedicament);
                            stmtAddStock.executeUpdate();
                        }
                    } else {
                        // Sinon, on met à jour le stock à 1
                        try (PreparedStatement stmtUpdateStock = connection.prepareStatement(sqlUpdateStock)) {
                            stmtUpdateStock.setInt(1, UserSession.getId());
                            stmtUpdateStock.setInt(2, idMedicament);
                            stmtUpdateStock.executeUpdate();
                        }
                    }
                }

                return true; // Succès si le médicament existe déjà et son stock a été mis à jour
            } else {
                // Si le médicament n'existe pas, on le crée
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

                            // Ajouter le stock initialisé à 1 pour la pharmacie donnée
                            try (PreparedStatement stmtStock = connection.prepareStatement(sqlStock)) {
                                stmtStock.setInt(1, UserSession.getId());
                                stmtStock.setInt(2, idMedicament);
                                stmtStock.executeUpdate();
                            }

                            return true; // Succès
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Échec
    }

    public static List<Medicament> getMedicamentsByOrdonnanceId(int ordonnanceId) {
        List<Medicament> medicaments = new ArrayList<>();
        String sql;

        // Build the SQL query based on the role
        if ("pharmacie".equals(UserSession.getRole())) {
            sql = "SELECT m.id, m.nom, m.description, m.prix, om.instructions, om.quantite, s.quantite AS stock " +
                    "FROM medicament m " +
                    "JOIN ordonnance_medicament om ON m.id = om.medicament_id " +
                    "JOIN stock s ON m.id = s.medicament_id " +
                    "WHERE om.ordonnance_id = ? AND s.pharmacie_id = ?";
        } else {
            sql = "SELECT m.id, m.nom, m.description, m.prix, om.instructions, om.quantite " +
                    "FROM medicament m " +
                    "JOIN ordonnance_medicament om ON m.id = om.medicament_id " +
                    "WHERE om.ordonnance_id = ?";
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set the first parameter (ordonnanceId)
            stmt.setInt(1, ordonnanceId);

            // If the role is "pharmacie", set the second parameter (pharmacie_id)
            if ("pharmacie".equals(UserSession.getRole())) {
                stmt.setInt(2, UserSession.getId());
            }

            // Execute the query
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Medicament medicament = new Medicament(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getInt("prix"),
                        "pharmacie".equals(UserSession.getRole()) ? rs.getInt("stock") : 0,  // Set stock only for pharmacies
                        rs.getString("instructions"),
                        rs.getInt("quantite")
                );
                medicaments.add(medicament);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return medicaments;
    }

}

