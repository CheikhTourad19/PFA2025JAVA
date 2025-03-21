package pfa.java.pfa2025java.dao;

import pfa.java.pfa2025java.model.DossierMedical;
import pfa.java.pfa2025java.model.TraitementRDV;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DossierMedicaleDAO {
    private static final Connection connection;

    static {
        try {
            connection = DBconnection.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addDossierMedical(DossierMedical dossier) {
        String query = "INSERT INTO dossier_medical (patient_id, groupe_sanguin, allergies, antecedents_medicaux, antecedents_familiaux) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, dossier.getPatientId());
            stmt.setString(2, dossier.getGroupeSanguin());
            stmt.setString(3, dossier.getAllergies());
            stmt.setString(4, dossier.getAntecedentsMedicaux());
            stmt.setString(5, dossier.getAntecedentsFamiliaux());
            stmt.executeUpdate();

            // Récupérer l'ID généré et le définir dans l'objet dossier
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dossier.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Récupérer un dossier médical par son ID
    public DossierMedical getDossierById(int id) {
        String query = "SELECT * FROM dossier_medical WHERE id = ?";
        DossierMedical dossier = null;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    dossier = new DossierMedical(
                            rs.getInt("id"),
                            rs.getInt("patient_id"),
                            rs.getString("groupe_sanguin"),
                            rs.getString("allergies"),
                            rs.getString("antecedents_medicaux"),
                            rs.getString("antecedents_familiaux"),
                              // Récupération des traitements associés
                            rs.getTimestamp("date_creation"),
                            Collections.singletonList((TraitementRDV) getTraitementsByDossierId(rs.getInt("id")))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dossier;
    }

    // Rechercher un dossier médical par patient_id
    public DossierMedical getDossierByPatientId(int patientId) {
        String query = "SELECT * FROM dossier_medical WHERE patient_id = ?";
        DossierMedical dossier = null;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int dossierId = rs.getInt("id");
                    dossier = new DossierMedical(
                            dossierId,
                            rs.getInt("patient_id"),
                            rs.getString("groupe_sanguin"),
                            rs.getString("allergies"),
                            rs.getString("antecedents_medicaux"),
                            rs.getString("antecedents_familiaux"),
                             // Récupérer les traitements associés
                            rs.getTimestamp("date_creation"),
                            Collections.singletonList((TraitementRDV) getTraitementsByDossierId(dossierId))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dossier;
    }

    // Mettre à jour les informations du dossier médical (hors traitements)
    public void updateDossierMedical(DossierMedical dossier) {
        String query = "UPDATE dossier_medical SET groupe_sanguin = ?, allergies = ?, antecedents_medicaux = ?, antecedents_familiaux = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, dossier.getGroupeSanguin());
            stmt.setString(2, dossier.getAllergies());
            stmt.setString(3, dossier.getAntecedentsMedicaux());
            stmt.setString(4, dossier.getAntecedentsFamiliaux());
            stmt.setInt(5, dossier.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un dossier médical
    public void deleteDossierMedical(int id) {
        String query = "DELETE FROM dossier_medical WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Récupérer les traitements associés à un dossier médical via la table d'association
    public List<TraitementRDV> getTraitementsByDossierId(int dossierId) {
        List<TraitementRDV> traitements = new ArrayList<>();
        String query = "SELECT t.* FROM traitements_rdv t " +
                "JOIN dossier_traitements_rdv dtr ON t.id = dtr.traitement_rdv_id " +
                "WHERE dtr.dossier_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, dossierId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Si la colonne date_creation n'existe pas dans traitements_rdv, ajustez le constructeur
                    TraitementRDV traitement = new TraitementRDV(
                            rs.getInt("id"),
                            rs.getInt("rdv_id"),
                            rs.getString("traitement"),
                            rs.getString("observations"),
                            rs.getTimestamp("date_creation")  // Assurez-vous que cette colonne existe, sinon retirez-la
                    );
                    traitements.add(traitement);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return traitements;
    }

    public void updateTraitementsDossier(int dossierId, List<Integer> traitementsIds) {
        String insertQuery = "INSERT INTO dossier_traitements_rdv (dossier_id, traitement_rdv_id) VALUES (?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {



            // Insérer les nouveaux traitements
            for (int traitementId : traitementsIds) {
                insertStmt.setInt(1, dossierId);
                insertStmt.setInt(2, traitementId);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
