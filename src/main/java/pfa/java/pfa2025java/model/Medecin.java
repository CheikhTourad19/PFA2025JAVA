package pfa.java.pfa2025java.model;

public class Medecin extends User{

    String service;
    public Medecin() {}
    public Medecin(int medecinID, String service) {
        this.service = service;
    }
    public Medecin(int id, String nom, String prenom, String email, String password, String role, String numero,String service){
        super(id,nom,prenom,email,password,role,numero);
        this.service = service;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
