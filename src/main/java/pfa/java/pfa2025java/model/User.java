package pfa.java.pfa2025java.model;

public class User {
    protected int id;
    protected String nom;
    protected String prenom;
    protected String email;
    protected String password;
    protected String role;
    protected String numero;
    private byte[] facialData;

    public byte[] getFacialData() {
        return facialData;
    }

    public void setFacialData(byte[] facialData) {
        this.facialData = facialData;
    }


    // Constructeur

    public User(int id, String nom, String prenom, String email, String password, String role, String numero) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.role = role;
        this.numero = numero;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}

