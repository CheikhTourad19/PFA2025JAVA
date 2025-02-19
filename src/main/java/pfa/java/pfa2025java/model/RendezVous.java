package pfa.java.pfa2025java.model;

import java.util.Date;

public class RendezVous {

    private int id;
    private int medecinId;
    private String medecinNom;
    private int patientId;
    private String patientNom;
    private Date date;
    private String etat;


    public RendezVous(int id, int medecinId, String medecinNom, int patientId, String patientNom, Date date, String etat) {
        this.id = id;
        this.medecinId = medecinId;
        this.medecinNom = medecinNom;
        this.patientId = patientId;
        this.patientNom = patientNom;
        this.date = date;
        this.etat = etat;
    }
}
