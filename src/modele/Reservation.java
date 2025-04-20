package modele;

import java.math.BigDecimal;
import java.util.Date;

public class Reservation {
    private Long id;
    private Utilisateur utilisateur;
    private Attraction attraction;
    private Date dateReservation;
    private Date heureReservation;
    private int nombrePersonnes;
    private BigDecimal prixTotal;
    private String statut; // "CONFIRMÉE", "ANNULÉE", "EN_ATTENTE"
    private String commentaire;
    private Date dateCreation;
    private Date dateModification;

    // Constructeurs
    public Reservation() {
        this.dateCreation = new Date();
        this.dateModification = new Date();
        this.statut = "EN_ATTENTE";
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Date getHeureReservation() {
        return heureReservation;
    }

    public void setHeureReservation(Date heureReservation) {
        this.heureReservation = heureReservation;
    }

    public int getNombrePersonnes() {
        return nombrePersonnes;
    }

    public void setNombrePersonnes(int nombrePersonnes) {
        this.nombrePersonnes = nombrePersonnes;
    }

    public BigDecimal getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(BigDecimal prixTotal) {
        this.prixTotal = prixTotal;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
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
        return "Reservation{" +
                "id=" + id +
                ", utilisateurId=" + (utilisateur != null ? utilisateur.getId() : null) +
                ", attractionId=" + (attraction != null ? attraction.getId() : null) +
                ", dateReservation=" + dateReservation +
                ", nombrePersonnes=" + nombrePersonnes +
                ", statut='" + statut + '\'' +
                '}';
    }
}
