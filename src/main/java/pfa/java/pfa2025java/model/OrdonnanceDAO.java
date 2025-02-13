package pfa.java.pfa2025java.model;

import pfa.java.pfa2025java.UserSession;

import java.sql.*;
import java.util.List;

public class OrdonnanceDAO {
    private static final Connection connection;


    static {
        try {
            connection = DBconnection.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Récupérer les détails d'une ordonnance
    public  static OrdonnanceDetails getOrdonnanceDetailsById(int ordonnanceId) {
        String sql = """
                SELECT o.id AS ordonnanceId,\s
                       u1.nom AS medecinNom,\s
                       u2.nom AS patientNom,\s
                       o.date_creation
                FROM ordonnance o
                JOIN user u1 ON o.medecin_id = u1.id  -- Récupérer le nom du médecin
                JOIN user u2 ON o.patient_id = u2.id  -- Récupérer le nom du patient
                WHERE o.id = ?;
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ordonnanceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String medecinNom = rs.getString("medecinNom");
                String patientNom = rs.getString("patientNom");
                String dateCreation = rs.getString("date_creation");

                // Récupérer la liste des médicaments associés à l'ordonnance
                List<Medicament> medicaments = MedicamentDAO.getMedicamentsByOrdonnanceId(ordonnanceId);

                // Retourner l'objet OrdonnanceDetails
                return new OrdonnanceDetails(ordonnanceId, medecinNom, patientNom, dateCreation, medicaments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean creerOrdonnanceParMedecin(int patientId, String dateCreation, List<Integer> medicamentIds) {
        int medecinId = UserSession.getId(); // Récupération de l'ID du médecin connecté
        return creerOrdonnance(medecinId, patientId, dateCreation, medicamentIds);
    }


    // Méthode privée qui effectue l'insertion en base de données
    private static boolean creerOrdonnance(int createurId, int patientId, String dateCreation, List<Integer> medicamentIds) {
        String insertOrdonnanceSQL = "INSERT INTO ordonnance (medecin_id, patient_id, date_creation) VALUES (?, ?, ?)";
        String insertOrdonnanceMedicamentSQL = "INSERT INTO ordonnance_medicament (ordonnance_id, medicament_id) VALUES (?, ?)";

        try (PreparedStatement stmtOrdonnance = connection.prepareStatement(insertOrdonnanceSQL, Statement.RETURN_GENERATED_KEYS)) {
            stmtOrdonnance.setInt(1, createurId);
            stmtOrdonnance.setInt(2, patientId);
            stmtOrdonnance.setString(3, dateCreation);

            int affectedRows = stmtOrdonnance.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("L'insertion de l'ordonnance a échoué, aucune ligne ajoutée.");
            }

            // Récupération de l'ID généré pour l'ordonnance
            ResultSet generatedKeys = stmtOrdonnance.getGeneratedKeys();
            if (generatedKeys.next()) {
                int ordonnanceId = generatedKeys.getInt(1);

                // Insérer les médicaments associés à l'ordonnance
                try (PreparedStatement stmtMedicament = connection.prepareStatement(insertOrdonnanceMedicamentSQL)) {
                    for (int medicamentId : medicamentIds) {
                        stmtMedicament.setInt(1, ordonnanceId);
                        stmtMedicament.setInt(2, medicamentId);
                        stmtMedicament.addBatch(); // Exécuter en batch pour optimiser
                    }
                    stmtMedicament.executeBatch();
                }
                return true;
            } else {
                throw new SQLException("L'insertion de l'ordonnance a échoué, aucun ID généré.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
