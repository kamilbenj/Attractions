package modele;

import java.math.BigDecimal;
import java.util.Date;

public class Attraction {
    private Long id;
    private String nom;
    private String description;
    private BigDecimal prix;
    private int capacite;
    private int duree; // en minutes
    private int ageMiniRequis;
    private int tailleMinRequise; // en cm
    private String imageUrl;
    private boolean estDisponible;
    private Date dateCreation;
    private Date dateModification;

    // Constructeurs
    // Constructeurs
    public Attraction() {
        this.dateCreation = new Date();
        this.dateModification = new Date();
        this.estDisponible = true;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public int getAgeMiniRequis() {
        return ageMiniRequis;
    }

    public void setAgeMiniRequis(int ageMiniRequis) {
        this.ageMiniRequis = ageMiniRequis;
    }

    public int getTailleMinRequise() {
        return tailleMinRequise;
    }

    public void setTailleMinRequise(int tailleMinRequise) {
        this.tailleMinRequise = tailleMinRequise;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isEstDisponible() {
        return estDisponible;
    }

    public void setEstDisponible(boolean estDisponible) {
        this.estDisponible = estDisponible;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    @Override
    public String toString() {
        return "Attraction{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", capacite=" + capacite +
                ", duree=" + duree +
                ", estDisponible=" + estDisponible +
                '}';
    }
}