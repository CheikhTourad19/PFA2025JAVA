package pfa.java.pfa2025java.model;
import java.util.List;

public class OrdonnanceDetails {
    private int ordonnanceId;
    private String medecinNom;
    private String patientNom;
    private String dateCreation;
    private List<Medicament> medicaments; // Liste des médicaments


    // Constructeur
    public OrdonnanceDetails(int ordonnanceId, String medecinNom, String patientNom, String dateCreation, List<Medicament> medicaments) {
        this.ordonnanceId = ordonnanceId;
        this.medecinNom = medecinNom;
        this.patientNom = patientNom;
        this.dateCreation = dateCreation;
        this.medicaments = medicaments;
    }

    // Getters et Setters
    public int getOrdonnanceId() { return ordonnanceId; }
    public void setOrdonnanceId(int ordonnanceId) { this.ordonnanceId = ordonnanceId; }

    public String getMedecinNom() { return medecinNom; }
    public void setMedecinNom(String medecinNom) { this.medecinNom = medecinNom; }

    public String getPatientNom() { return patientNom; }
    public void setPatientNom(String patientNom) { this.patientNom = patientNom; }

    public String getDateCreation() { return dateCreation; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation; }

    public List<Medicament> getMedicaments() { return medicaments; }
    public void setMedicaments(List<Medicament> medicaments) { this.medicaments = medicaments; }

    // Méthode toString() pour affichage
    @Override
    public String toString() {
        return "OrdonnanceDetails{" +
                "ordonnanceId=" + ordonnanceId +
                ", medecinNom='" + medecinNom + '\'' +
                ", patientNom='" + patientNom + '\'' +
                ", dateCreation='" + dateCreation + '\'' +
                ", medicaments=" + medicaments +
                '}';
    }
}