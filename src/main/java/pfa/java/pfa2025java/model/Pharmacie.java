package pfa.java.pfa2025java.model;

public class Pharmacie extends User{

    private Adresse adresse;

    public Pharmacie(int id, String nom, String prenom, String email, String password, String role, String numero, Adresse adresse) {
        super(id, nom, prenom, email, password, role, numero);
        this.adresse = adresse;
    }

    public Adresse getAdresse() {
        return adresse;
    }


}
