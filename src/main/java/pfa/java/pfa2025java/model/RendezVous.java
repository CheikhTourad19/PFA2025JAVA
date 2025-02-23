package pfa.java.pfa2025java.model;

public class RendezVous {
    private int id;
    private int medecinId;
    private int patientId;
    private String statut;
    private String medecinNom;

    public RendezVous(int id, int medecinId, int patientId, String statut, String medecinNom) {
        this.id = id;
        this.medecinId = medecinId;
        this.patientId = patientId;
        this.statut = statut;
        this.medecinNom = medecinNom;
    }

    public int getId() {
        return id;
    }

    public int getMedecinId() {
        return medecinId;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getStatut() {
        return statut;
    }

    public String getMedecinNom() {
        return medecinNom;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
