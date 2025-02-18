package pfa.java.pfa2025java;

public class UserSession {
    static private int id;
    static private String nom;
    static private String prenom;
    static private String email;
    static private String password;
    static private String role;
    static private String numero;

    public UserSession(int id, String nom, String prenom, String email, String password, String role, String numero) {
        UserSession.id = id;
        UserSession.nom = nom;
        UserSession.prenom = prenom;
        UserSession.email = email;
        UserSession.password = password;
        UserSession.role = role;
        UserSession.numero = numero;
    }

    public static String getNumero() {
        return numero;
    }

    public UserSession() {
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        UserSession.id = id;
    }

    public static void setNumero(String numero) {
        UserSession.numero = numero;
    }

    public static String getNom() {
        return nom;
    }

    public static void setNom(String nom) {
        UserSession.nom = nom;
    }

    public static String getPrenom() {
        return prenom;
    }

    public static void setPrenom(String prenom) {
        UserSession.prenom = prenom;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserSession.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        UserSession.password = password;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        UserSession.role = role;
    }

    public static void logout() {
        UserSession.id = 0;
        UserSession.nom = null;
        UserSession.prenom = null;
        UserSession.email = null;
        UserSession.password = null;
        UserSession.role = null;
    }
}
