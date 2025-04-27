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


    public int getPourcentage() {
        return pourcentage;
    }


    public CritereReduction getCritere() {
        return critere;
    }

    @Override
    public String toString() {
        return nom + " (" + pourcentage + "% - " + critere + ")";
    }
}