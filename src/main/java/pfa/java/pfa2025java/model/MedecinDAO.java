package pfa.java.pfa2025java.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pfa.java.pfa2025java.UserSession;


public class MedecinDAO {
    private static final Connection connection;

    static {
        try {
            connection = DBconnection.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getAllSpecialties() {
        String sql = "SELECT DISTINCT service FROM medecin"; // Assurez-vous que 'service' est bien la colonne contenant les spécialités
        List<String> specialties = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                specialties.add(rs.getString("service")); // Ajoute chaque spécialité trouvée
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return specialties;
    }

    public static void sendRDVRequest(int medecinId, int patientId) {
        String sql = "INSERT INTO rendezvous (medecin_id, patient_id, statut) VALUES (?, ?, 'En attente')";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, medecinId);
            stmt.setInt(2, patientId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateRDVStatus(int rdvId, String statut) {
        String sql = "UPDATE rendezvous SET statut = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, statut);
            stmt.setInt(2, rdvId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<RendezVous> getPendingRendezVous(int medecinId) {
        List<RendezVous> rendezVousList = new ArrayList<>();
        String sql = "SELECT rdv.id, rdv.medecin_id, rdv.patient_id, rdv.statut, u.nom AS patientNom " +
                "FROM rendezvous rdv " +
                "JOIN user u ON rdv.patient_id = u.id " +
                "WHERE rdv.medecin_id = ? AND rdv.statut = 'En attente'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, medecinId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rendezVousList.add(new RendezVous(
                        rs.getInt("id"),
                        rs.getInt("medecin_id"),
                        rs.getInt("patient_id"),
                        rs.getString("statut"),
                        rs.getString("patientNom")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rendezVousList;
    }

    public static List<RendezVous> getAcceptedRendezVous(int patientId) {
        List<RendezVous> rendezVousList = new ArrayList<>();
        String sql = "SELECT rdv.id, rdv.medecin_id, rdv.patient_id, rdv.statut, u.nom AS medecinNom " +
                "FROM rendezvous rdv " +
                "JOIN medecin m ON rdv.medecin_id = m.medecin_id " +
                "JOIN user u ON m.medecin_id = u.id " +
                "WHERE rdv.patient_id = ? AND rdv.statut = 'Accepté'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rendezVousList.add(new RendezVous(
                        rs.getInt("id"),
                        rs.getInt("medecin_id"),
                        rs.getInt("patient_id"),
                        rs.getString("statut"),
                        rs.getString("medecinNom")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rendezVousList;
    }

    public static List<Medecin> getAllMedecin() throws SQLException {
        String sql = "SELECT * FROM medecin m JOIN user u ON m.medecin_id = u.id";
        List<Medecin> medecins = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Récupérer les données à partir de ResultSet
                Medecin medecin = new Medecin(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("numero"),
                        rs.getString("service")
                );

                // Ajouter le médecin à la liste
                medecins.add(medecin);
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching medecins from the database", e);
        }

        return medecins;
    }

}
