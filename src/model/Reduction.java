package model;

public class Reduction {

    private int id;
    private String nom;
    private int pourcentage;
    private CritereReduction critere;

    // Enumération des critères possibles
    public enum CritereReduction {
        ENFANT, SENIOR, FIDELITE
    }

    // Constructeurs
    public Reduction() {}

    public Reduction(int id, String nom, int pourcentage, CritereReduction critere) {
        this.id = id;
        this.nom = nom;
        this.pourcentage = pourcentage;
        this.critere = critere;
    }

    public Reduction(String nom, int pourcentage, CritereReduction critere) {
        this.nom = nom;
        this.pourcentage = pourcentage;
        this.critere = critere;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(int pourcentage) {
        this.pourcentage = pourcentage;
    }

    public CritereReduction getCritere() {
        return critere;
    }

    public void setCritere(CritereReduction critere) {
        this.critere = critere;
    }

    @Override
    public String toString() {
        return nom + " (" + pourcentage + "% - " + critere + ")";
    }
}