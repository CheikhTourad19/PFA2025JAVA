package pfa.java.pfa2025java.dao;



import pfa.java.pfa2025java.model.TraitementRDV;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TraitementRDVDAO {

    // Ajouter un traitement RDV
    public void addTraitementRDV(TraitementRDV traitement) {
        String query = "INSERT INTO traitements_rdv (rdv_id, traitement, observations) VALUES (?, ?, ?)";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, traitement.getRdvId());
            stmt.setString(2, traitement.getTraitement());
            stmt.setString(3, traitement.getObservations());

            stmt.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    traitement.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Récupérer un traitement RDV par ID
    public TraitementRDV getTraitementRDVById(int id) {
        String query = "SELECT * FROM traitements_rdv WHERE id = ?";
        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new TraitementRDV(
                        rs.getInt("id"),
                        rs.getInt("rdv_id"),
                        rs.getString("traitement"),
                        rs.getString("observations"),
                        rs.getTimestamp("date_creation")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Récupérer tous les traitements RDV
    public List<TraitementRDV> getAllTraitementsRDV() {
        List<TraitementRDV> traitements = new ArrayList<>();
        String query = "SELECT * FROM traitements_rdv";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                traitements.add(new TraitementRDV(
                        rs.getInt("id"),
                        rs.getInt("rdv_id"),
                        rs.getString("traitement"),
                        rs.getString("observations"),
                        rs.getTimestamp("date_creation")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return traitements;
    }

    // Récupérer les traitements liés à un dossier médical via `dossier_traitements_rdv`
    public List<TraitementRDV> getTraitementsByDossierId(int dossierId) {
        List<TraitementRDV> traitements = new ArrayList<>();
        String query = "SELECT t.* FROM traitements_rdv t " +
                       "JOIN dossier_traitements_rdv dtr ON t.id = dtr.traitement_rdv_id " +
                       "WHERE dtr.dossier_id = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, dossierId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                traitements.add(new TraitementRDV(
                        rs.getInt("id"),
                        rs.getInt("rdv_id"),
                        rs.getString("traitement"),
                        rs.getString("observations"),
                        rs.getTimestamp("date_creation")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return traitements;
    }

    // Mettre à jour un traitement RDV
    public void updateTraitementRDV(TraitementRDV traitement) {
        String query = "UPDATE traitements_rdv SET rdv_id = ?, traitement = ?, observations = ? WHERE id = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, traitement.getRdvId());
            stmt.setString(2, traitement.getTraitement());
            stmt.setString(3, traitement.getObservations());
            stmt.setInt(4, traitement.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un traitement RDV
    public void deleteTraitementRDV(int id) {
        String query = "DELETE FROM traitements_rdv WHERE id = ?";

        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
