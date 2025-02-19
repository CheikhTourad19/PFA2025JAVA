package pfa.java.pfa2025java.model;


public class RendezVous {
    private int id;
    private String date;
    private String heure;
    private String medecin;

    public RendezVous(int id, String date, String heure, String medecin) {
        this.id = id;
        this.date = date;
        this.heure = heure;
        this.medecin = medecin;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getHeure() { return heure; }
    public void setHeure(String heure) { this.heure = heure; }

    public String getMedecin() { return medecin; }
    public void setMedecin(String medecin) { this.medecin = medecin; }
}
