package pfa.java.pfa2025java.model;

public class RendezVous {
    private int id;
    private int medecinId;
    private String medecinNom;
    private int patientId;
    private String statut;
    private String date;

    public RendezVous(int id, int medecinId, int patientId, String statut, String date) {
        this.id = id;
        this.medecinId = medecinId;
        this.patientId = patientId;
        this.statut = statut;
        this.date = date;
    }

    public String getMedecinNom() {
        return medecinNom;
    }

    public void setMedecinNom(String medecinNom) {
        this.medecinNom = medecinNom;
    }

    public RendezVous(int id, String medecinNom, int patientId, String statut, String date) {
        this.id = id;
        this.medecinNom = medecinNom;
        this.patientId = patientId;
        this.statut = statut;
        this.date = date;
    }

    public RendezVous(int id, String medecinNom, String statut, String date) {
        this.id = id;
        this.medecinNom = medecinNom;
        this.statut = statut;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getMedecinId() {
        return medecinId;
    }

    public void setMedecinId(int medecinId) {
        this.medecinId = medecinId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}