package pfa.java.pfa2025java.model;

public class Infermier extends User{

    private String service;

    public Infermier(int id, String nom, String prenom, String email, String password, String role, String numero, String service) {
        super(id, nom, prenom, email, password, role, numero);
        this.service = service;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
