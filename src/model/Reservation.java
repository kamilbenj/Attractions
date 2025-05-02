package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Représente une réservation faite par un utilisateur pour une attraction donnée
 * Contient les informations telles que la date, l'heure, le nombre de billets
 * l'état de la réservation, et les identifiants liés (utilisateur, attraction)
 * Cette classe est utilisée comme modèle métier dans le système
 * Elle peut être manipulée via {@code ReservationDAO}
 *
 * @see dao.ReservationDAO
 * @see model.Reservation.StatutReservation
 */
public class Reservation {

    private int id;
    private int idUtilisateur;
    private int idAttraction;
    private LocalDate dateReservation;
    private LocalTime heureReservation; // Nouvel attribut
    private int nombreBillets;
    private StatutReservation statut;

    public enum StatutReservation {//état de la réservation
        CONFIRMEE, ANNULEE, EN_ATTENTE
    }

    /**
     * Constructeur utilisé lors de la création d'une nouvelle réservation.
     *
     * @param idUtilisateur Identifiant de l'utilisateur (0 si invité)
     * @param idAttraction Identifiant de l'attraction
     * @param dateReservation Date prévue pour la réservation
     * @param heureReservation Heure prévue (peut être {@code null})
     * @param nombreBillets Nombre de billets réservés
     * @param statut Statut initial de la réservation
     */
    public Reservation(int idUtilisateur, int idAttraction, LocalDate dateReservation, LocalTime heureReservation, int nombreBillets, StatutReservation statut) {
        this.idUtilisateur = idUtilisateur;
        this.idAttraction = idAttraction;
        this.dateReservation = dateReservation;
        this.heureReservation = heureReservation;
        this.nombreBillets = nombreBillets;
        this.statut = statut;
    }

    /**
     * Constructeur utilisé lors du chargement d'une réservation existante (avec ID).
     *
     * @param id Identifiant unique de la réservation
     * @param idUtilisateur Identifiant de l'utilisateur
     * @param idAttraction Identifiant de l'attraction
     * @param dateReservation Date de la réservation
     * @param heureReservation Heure prévue (peut être {@code null})
     * @param nombreBillets Nombre de billets
     * @param statut Statut actuel
     */
    public Reservation(int id, int idUtilisateur, int idAttraction, LocalDate dateReservation, LocalTime heureReservation, int nombreBillets, StatutReservation statut) {
        this(idUtilisateur, idAttraction, dateReservation, heureReservation, nombreBillets, statut);
        this.id = id;
    }

    /** getters et setters */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public int getIdAttraction() {
        return idAttraction;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public LocalTime getHeureReservation() {
        return heureReservation;
    }

    public int getNombreBillets() {
        return nombreBillets;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    /**
     * Fournit une description lisible de la réservation
     * @return Une chaîne résumant la réservation (id, attraction, heure, date)
     */
    @Override
    public String toString() {
        return "Réservation #" + id + " : Attraction " + idAttraction + " à " + heureReservation + "h le " + dateReservation;
    }
}