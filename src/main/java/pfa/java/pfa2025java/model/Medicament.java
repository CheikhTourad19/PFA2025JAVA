package pfa.java.pfa2025java.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Medicament {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final IntegerProperty prix = new SimpleIntegerProperty();
    private final IntegerProperty stock = new SimpleIntegerProperty();
    private final StringProperty instruction = new SimpleStringProperty();
    private final IntegerProperty quantite = new SimpleIntegerProperty();

    //basic medicament
    public Medicament(int id, String nom, String description, int prix) {
        this.id.set(id);
        this.nom.set(nom);
        this.description.set(description);
        this.prix.set(prix);
    }

    //Pour stock
    public Medicament(int id, String nom, String description, int prix, int stock) {
        this.id.set(id);
        this.nom.set(nom);
        this.description.set(description);
        this.prix.set(prix);
        this.stock.set(stock);
    }

    //Pour Ordonnance
    public Medicament(int id, String nom, String description, int prix, int stock, String instruction, int quantite) {
        this.id.set(id);
        this.nom.set(nom);
        this.description.set(description);
        this.prix.set(prix);
        this.stock.set(stock);
        this.instruction.set(instruction);
        this.quantite.set(quantite);
    }

    public IntegerProperty quantiteProperty() {
        return quantite;
    }

    // Getters et setters pour JavaFX TableView
    public IntegerProperty idProperty() {
        return id;
    }

    public String getInstruction() {
        return instruction.get();
    }

    public StringProperty instructionProperty() {
        return instruction;
    }





    public StringProperty nomProperty() {
        return nom;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public IntegerProperty prixProperty() {
        return prix;
    }

    public IntegerProperty stockProperty() {
        return stock;
    }

    // Getters et setters classiques (si nécessaire)
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setQuantite(int Quantite) {
        this.quantite.set(Quantite);
    }

    public void setInstruction(String instruction) {
        this.instruction.set(instruction);
    }
    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public int getPrix() {
        return prix.get();
    }

    public void setPrix(int prix) {
        this.prix.set(prix);
    }

    public int getStock() {
        return stock.get();
    }

    public void setStock(int stock) {
        this.stock.set(stock);
    }

    public int getQuantite() {
        return quantite.get();
    }

    public String toString() {
        return this.nom.get() + " " + stock.get();
    }
}
