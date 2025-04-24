package model;

public class Attraction {

    private int id;
    private String nom;
    private String description;
    private double prix;
    private int capacite;
    private boolean disponible;

    // Constructeurs
    public Attraction() {
    }

    public Attraction(int id, String nom, String description, double prix, int capacite, boolean disponible) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.capacite = capacite;
        this.disponible = disponible;
    }

    public Attraction(String nom, String description, double prix, int capacite, boolean disponible) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.capacite = capacite;
        this.disponible = disponible;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return nom + " - " + prix + "€ [" + (disponible ? "Ouvert" : "Fermé") + "]";
    }
}