package pfa.java.pfa2025java.model;

import java.sql.Timestamp;
import java.util.List;

public class DossierMedical {
    private int id;
    private int patientId;
    private String groupeSanguin;
    private String allergies;
    private String antecedentsMedicaux;
    private String antecedentsFamiliaux;
    private Timestamp dateCreation;
    private List<TraitementRDV> traitementsRDV;

    // Constructeur
    public DossierMedical(int id, int patientId, String groupeSanguin, String allergies,
                          String antecedentsMedicaux, String antecedentsFamiliaux, Timestamp dateCreation) {
        this.id = id;
        this.patientId = patientId;
        this.groupeSanguin = groupeSanguin;
        this.allergies = allergies;
        this.antecedentsMedicaux = antecedentsMedicaux;
        this.antecedentsFamiliaux = antecedentsFamiliaux;
        this.dateCreation = dateCreation;
    }

    public DossierMedical(int id, int patientId, String groupeSanguin, String allergies, String antecedentsMedicaux, String antecedentsFamiliaux, Timestamp dateCreation, List<TraitementRDV> traitementsRDV) {
        this.id = id;
        this.patientId = patientId;
        this.groupeSanguin = groupeSanguin;
        this.allergies = allergies;
        this.antecedentsMedicaux = antecedentsMedicaux;
        this.antecedentsFamiliaux = antecedentsFamiliaux;
        this.dateCreation = dateCreation;
        this.traitementsRDV = traitementsRDV;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getGroupeSanguin() { return groupeSanguin; }
    public void setGroupeSanguin(String groupeSanguin) { this.groupeSanguin = groupeSanguin; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    public String getAntecedentsMedicaux() { return antecedentsMedicaux; }
    public void setAntecedentsMedicaux(String antecedentsMedicaux) { this.antecedentsMedicaux = antecedentsMedicaux; }

    public String getAntecedentsFamiliaux() { return antecedentsFamiliaux; }
    public void setAntecedentsFamiliaux(String antecedentsFamiliaux) { this.antecedentsFamiliaux = antecedentsFamiliaux; }

    public Timestamp getDateCreation() { return dateCreation; }
    public void setDateCreation(Timestamp dateCreation) { this.dateCreation = dateCreation; }

    public List<TraitementRDV> getTraitementsRDV() { return traitementsRDV; }
    public void setTraitementsRDV(List<TraitementRDV> traitementsRDV) { this.traitementsRDV = traitementsRDV; }
}
