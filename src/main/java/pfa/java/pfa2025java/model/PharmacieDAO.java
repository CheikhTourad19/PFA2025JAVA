package pfa.java.pfa2025java.model;

import pfa.java.pfa2025java.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class PharmacieDAO {

    private static Connection connection;

    static {
        try{
            connection=DBconnection.connect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static boolean addPharmacie(User m) {
        int id = UserDAO.getUserByEmail(m.getEmail()).getId();
        String sqlMedecin = "INSERT INTO pharmacie (pharmacie_id,cin) VALUES (?,?) ";
        try (PreparedStatement stmtMedecin = connection.prepareStatement(sqlMedecin)) {
            stmtMedecin.setInt(1, id);
            stmtMedecin.setString(2, "gg");
            stmtMedecin.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean UpdatePharmacieAdresse(String rue, String ville, String quartier) {
        String checkSql = "SELECT adresse_id FROM pharmacie WHERE pharmacie_id = ?";
        String insertSql = "INSERT INTO adresse (rue, ville, quartier) VALUES (?, ?, ? )";
        String updateSql = "UPDATE adresse SET rue=?, ville=?, quartier=? WHERE id=?";
        String updatePharmacieSql = "UPDATE pharmacie SET adresse_id = ? WHERE pharmacie_id = ?";

        try {
            connection.setAutoCommit(false); // Démarrer la transaction

            try (PreparedStatement stmt = connection.prepareStatement(checkSql)) {
                stmt.setInt(1, UserSession.getId());
                ResultSet rs = stmt.executeQuery();

                if (rs.next() && rs.getInt("adresse_id") > 0) {
                    // Mise à jour de l'adresse existante
                    int adresseId = rs.getInt("adresse_id");
                    try (PreparedStatement stmt2 = connection.prepareStatement(updateSql)) {
                        stmt2.setString(1, rue);
                        stmt2.setString(2, ville);
                        stmt2.setString(3, quartier);
                        stmt2.setInt(4, adresseId);
                        if (stmt2.executeUpdate() > 0) {
                            connection.commit(); // Valider la transaction
                            return true;
                        }
                    }
                } else {
                    // Insertion d'une nouvelle adresse
                    try (PreparedStatement stmt3 = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                        stmt3.setString(1, rue);
                        stmt3.setString(2, ville);
                        stmt3.setString(3, quartier);
                        stmt3.executeUpdate();

                        ResultSet generatedKeys = stmt3.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int newAdresseId = generatedKeys.getInt(1);
                            try (PreparedStatement stmt4 = connection.prepareStatement(updatePharmacieSql)) {
                                stmt4.setInt(1, newAdresseId);
                                stmt4.setInt(2, UserSession.getId());
                                if (stmt4.executeUpdate() > 0) {
                                    connection.commit(); // Valider la transaction
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            connection.rollback(); // Annuler en cas d'échec
        } catch (SQLException ex) {
            try {
                connection.rollback(); // En cas d'erreur, rollback
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new RuntimeException(ex);
        } finally {
            try {
                connection.setAutoCommit(true); // Réactiver l'auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public  static Adresse getPharmacieAdresse() throws SQLException {

        String checkSql = "SELECT adresse_id FROM pharmacie where pharmacie_id = ?";
        String getadresseSql = "SELECT * FROM adresse WHERE id=?";

        PreparedStatement stmt = connection.prepareStatement(checkSql);
        stmt.setInt(1, UserSession.getId());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int adresseId = rs.getInt("adresse_id");
            try (PreparedStatement stmt2 = connection.prepareStatement(getadresseSql)) {
                stmt2.setInt(1, adresseId);
                ResultSet rs2 = stmt2.executeQuery();
                if (rs2.next()) {
                    return new Adresse(rs2.getString("rue"),rs2.getString("ville"), rs2.getString("quartier"),rs2.getInt("id") );
                }
            }
        }
        return null;
    }



}
