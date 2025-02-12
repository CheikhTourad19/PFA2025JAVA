package pfa.java.pfa2025java.model;

public class Adresse {
    private String rue;
    private String ville;
    private String quartier;
    private int id;

    public Adresse(String rue, String ville, String quartier, int id) {
        this.rue = rue;
        this.ville = ville;
        this.quartier = quartier;
        this.id = id;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
