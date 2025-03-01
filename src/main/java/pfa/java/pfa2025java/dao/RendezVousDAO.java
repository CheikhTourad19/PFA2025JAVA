package pfa.java.pfa2025java.dao;

import pfa.java.pfa2025java.model.RendezVous;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RendezVousDAO {
    private static final Connection connection;

    static {
        try {
            connection = DBconnection.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static RendezVous getRdvById(int id) throws SQLException {
        String sql = "select * from rendez_vous where id = ?";
        try(PreparedStatement stmt =connection.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new RendezVous(
                        rs.getInt("id"),
                        rs.getInt("medecin_id"),
                        rs.getInt("patient_id"),
                        rs.getString("statut"),
                        rs.getString("date")

                );
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return null;
    }

    public static List<RendezVous> getRDVByPatientId(int patientId) throws SQLException {
        List<RendezVous> list = new ArrayList<>();
        String sql ="SELECT r.id, CONCAT(u.nom,' ',u.prenom) AS medecinNom, " +
                "r.patient_id, r.statut, r.date " +
                "FROM rendez_vous r "+
                "JOIN user u ON r.medecin_id = u.id " +
                "WHERE r.patient_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new RendezVous(
                        rs.getInt("id"),
                        rs.getString("medecinNom"),
                        rs.getString("statut"),
                        rs.getString("date")
                ));
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return list;
    }





    public static boolean updateRdvState(int id, String status) throws SQLException {
        String sql = "UPDATE rendez_vous SET statut = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }
    public static boolean updateRendezVous(RendezVous rdv) throws SQLException {
        String sql = "UPDATE rendez_vous SET medecin_id = ?, patient_id = ?, statut = ?, date = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rdv.getMedecinId());
            stmt.setInt(2, rdv.getPatientId());
            stmt.setString(3, rdv.getStatut());
            stmt.setString(4, rdv.getDate());
            stmt.setInt(5, rdv.getId());
            return stmt.executeUpdate() > 0;
        }
    }
    public static List<RendezVous> getRendezVousConfirmes(int medecinId) throws SQLException {
        List<RendezVous> list = new ArrayList<>();
        String sql = "SELECT * FROM rendez_vous WHERE medecin_id = ? AND statut = 'confirmé'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, medecinId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new RendezVous(
                        rs.getInt("id"),
                        rs.getInt("medecin_id"),
                        rs.getInt("patient_id"),
                        rs.getString("statut"),
                        rs.getString("date")
                ));
            }
        }
        return list;
    }

    public static List<RendezVous> getDemandesRendezVous(int medecinId) throws SQLException {
        List<RendezVous> list = new ArrayList<>();
        String sql = "SELECT * FROM rendez_vous WHERE medecin_id = ? AND statut = 'attente'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, medecinId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new RendezVous(
                        rs.getInt("id"),
                        rs.getInt("medecin_id"),
                        rs.getInt("patient_id"),
                        rs.getString("statut"),
                        rs.getString("date")
                ));
            }
        }
        return list;
    }

    public static boolean deleteRendezVous(int id) throws SQLException {
        String sql = "DELETE FROM rendez_vous WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}

