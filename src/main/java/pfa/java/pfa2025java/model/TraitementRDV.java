package pfa.java.pfa2025java.model;

import java.sql.Timestamp;

public class TraitementRDV {
    private int id;
    private int rdvId;
    private String traitement;
    private String observations;
    private Timestamp dateCreation;

    // Constructeur
    public TraitementRDV(int id, int rdvId, String traitement, String observations, Timestamp dateCreation) {
        this.id = id;
        this.rdvId = rdvId;
        this.traitement = traitement;
        this.observations = observations;
        this.dateCreation = dateCreation;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRdvId() { return rdvId; }
    public void setRdvId(int rdvId) { this.rdvId = rdvId; }

    public String getTraitement() { return traitement; }
    public void setTraitement(String traitement) { this.traitement = traitement; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public Timestamp getDateCreation() { return dateCreation; }
    public void setDateCreation(Timestamp dateCreation) { this.dateCreation = dateCreation; }
}
