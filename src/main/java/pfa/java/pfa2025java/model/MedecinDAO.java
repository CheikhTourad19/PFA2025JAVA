package pfa.java.pfa2025java.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MedecinDAO {
    private static final Connection connection;

    static {
        try {
            connection = DBconnection.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Medecin getMedecin(int id) {
        String sql = "SELECT * FROM medecin m \n" +
                "JOIN user u ON m.medecin_id = u.id \n" +
                "WHERE m.medecin_id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return new Medecin(rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("numero"),
                        rs.getString("service")
                        );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public static List<Medecin> getAllMedecin() {
        String sql = "SELECT * FROM medecin m JOIN user u ON m.medecin_id = u.id ";
        List<Medecin> medecins= new ArrayList<Medecin>();
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs =stmt.executeQuery();
            while(rs.next()) {
                medecins.add(new Medecin(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("numero"),
                        rs.getString("service")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    return medecins;
    }

    public static void addMedecin(Medecin m) {
        UserDAO.registerUser(m.getNom(), m.getPrenom(), m.getEmail(), m.getPassword(), m.getRole());
        int idMedecin = Objects.requireNonNull(UserDAO.getUserByEmail(m.getEmail())).getId();
        String sqlMedecin = "INSERT INTO medecin (service) VALUES (?) WHERE medecin_id = ?";
        try (PreparedStatement stmtMedecin = connection.prepareStatement(sqlMedecin)) {
            stmtMedecin.setString(1, m.getService());
            stmtMedecin.setInt(2, idMedecin);
            stmtMedecin.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteMedecin(int id) {
        String sqlUser = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement stmtUser = connection.prepareStatement(sqlUser)) {
            stmtUser.setInt(1, id);
            stmtUser.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateMedecin(int id, String nom, String prenom, String email, String password, String numero, String service) {
        String sqlUser = "UPDATE user SET nom = ?, prenom = ?, email = ?, password = ?, numero = ? WHERE id = ?";
        String sqlMedecin = "UPDATE medecin SET service = ? WHERE medecin_id = ?";
        try (PreparedStatement stmtUser = connection.prepareStatement(sqlUser);
             PreparedStatement stmtMedecin = connection.prepareStatement(sqlMedecin)) {
            stmtUser.setString(1, nom);
            stmtUser.setString(2, prenom);
            stmtUser.setString(3, email);
            stmtUser.setString(4, password);
            stmtUser.setString(5, numero);
            stmtUser.setInt(6, id);
            stmtUser.executeUpdate();

            stmtMedecin.setString(1, service);
            stmtMedecin.setInt(2, id);
            stmtMedecin.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getAllSpecialties() {
        String sql = "SELECT DISTINCT service FROM medecin";
        List<String> specialties = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                specialties.add(rs.getString("service"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return specialties;
    }

    public static void sendRDVRequest(int medecinId, int patientId) {
        String sql = "INSERT INTO rdv (medecin_id, patient_id, status) VALUES (?, ?, 'Pending')";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, medecinId);
            stmt.setInt(2, patientId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateRDVStatus(int id, String accepté) {
        String sql = "UPDATE rdv SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accepté);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static RendezVous getAcceptedRendezVous(int id) {
        String sql = "SELECT * FROM rdv WHERE id = ? AND status = 'Accepted'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Assuming the RendezVous object has fields for id, medecin_id, patient_id, and status
                return new RendezVous(
                        rs.getInt("id"),
                        rs.getInt("medecin_id"),
                        rs.getInt("patient_id"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // return null if no accepted appointment is found
    }

}

