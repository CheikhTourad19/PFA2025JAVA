package pfa.java.pfa2025java.dao;

import pfa.java.pfa2025java.UserSession;
import pfa.java.pfa2025java.model.PasswordUtils;
import pfa.java.pfa2025java.model.User;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDAO {
    private static final Connection connection;

    static {
        try {
            connection = DBconnection.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static User getUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("numero")// Le mot de passe est stocké sous forme hashée
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean login(String email, String password) {
        User user = getUserByEmail(email);
        return user != null && PasswordUtils.checkPassword(password, user.getPassword());
    }

    public static boolean registerUser(String nom, String email, String password, String prenom, String numero, String role) {
        String hashedPassword = PasswordUtils.hashPassword(password);  // Hashage du mot de passe
        String sql = "INSERT INTO user (nom, email, password, prenom, numero, role) VALUES (?, ?, ?, ?, ?, ?)";
        String sql2 = "INSERT INTO patient (patient_id, cin) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nom);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, prenom);
            stmt.setString(5, numero);
            stmt.setString(6, role);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);


                        try (PreparedStatement stmt2 = connection.prepareStatement(sql2)) {
                            stmt2.setInt(1, userId);
                            stmt2.setString(2, "0");
                            stmt2.executeUpdate();
                        }


                    return true;
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }




    public static boolean changePassword(String newPassword,int id) throws SQLException {
        String sql = "UPDATE user SET password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, PasswordUtils.hashPassword(newPassword));
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }

    }

    public static boolean changePrenomNomNumero(String newPrenom, String newNom, String newNumero) throws SQLException {
        String sql = "UPDATE user SET prenom = ?, nom = ?, numero = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPrenom);
            stmt.setString(2, newNom);
            stmt.setString(3, newNumero);
            stmt.setInt(4, UserSession.getId());
            return stmt.executeUpdate() > 0;
        }
    }
    public static boolean updateEmail(String newEmail) throws SQLException {
        String sql = "UPDATE user SET email = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, newEmail);
            stmt.setInt(2, UserSession.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("numero")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static boolean deleteUser(int userId) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public static InputStream getUserImage(int userId) {
        String query = "SELECT image FROM user_images WHERE user_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                byte[] imageBytes = rs.getBytes("image");
                if (imageBytes != null) {
                    return new ByteArrayInputStream(imageBytes);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean saveUserImage(int userId, File imageFile) {
        String query = "REPLACE INTO user_images (user_id, image) VALUES (?, ?)";
        try (
                PreparedStatement pstmt = connection.prepareStatement(query);
                FileInputStream fis = new FileInputStream(imageFile)) {

            pstmt.setInt(1, userId);
            pstmt.setBinaryStream(2, fis, (int) imageFile.length());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static User getUserByFacialData(byte[] facialData) {
        String sql = "SELECT * FROM user WHERE facial_data = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBytes(1, facialData);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("numero")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean saveUserFacialData(int userId, byte[] facialData) {
        String sql = "UPDATE user SET facial_data = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBytes(1, facialData);
            stmt.setInt(2, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
