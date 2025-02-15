package pfa.java.pfa2025java.model;

public class Medecin extends User{
    int medecinID;
    String service;
    public Medecin() {}
    public Medecin(int medecinID, String service) {
        this.medecinID = medecinID;
        this.service = service;
    }
    public Medecin(int id, String nom, String prenom, String email, String password, String role, String numero,String service){
        super(id,nom,prenom,email,password,role,numero);
        this.medecinID = id;
        this.service = service;
    }

    public int getMedecinID() {
        return medecinID;
    }

    public void setMedecinID(int medecinID) {
        this.medecinID = medecinID;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
