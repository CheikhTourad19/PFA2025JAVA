package pfa.java.pfa2025java.model;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    // Hacher un mot de passe
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Vérifier si un mot de passe correspond au hash stocké
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
