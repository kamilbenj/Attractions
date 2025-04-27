package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {

    private int id;
    private int idUtilisateur;
    private int idAttraction;
    private LocalDate dateReservation;
    private LocalTime heureReservation; // 🔥 Nouvel attribut
    private int nombreBillets;
    private StatutReservation statut;

    public enum StatutReservation {
        CONFIRMEE, ANNULEE, EN_ATTENTE
    }

    // === Constructeurs ===


    public Reservation(int idUtilisateur, int idAttraction, LocalDate dateReservation, LocalTime heureReservation, int nombreBillets, StatutReservation statut) {
        this.idUtilisateur = idUtilisateur;
        this.idAttraction = idAttraction;
        this.dateReservation = dateReservation;
        this.heureReservation = heureReservation;
        this.nombreBillets = nombreBillets;
        this.statut = statut;
    }

    public Reservation(int id, int idUtilisateur, int idAttraction, LocalDate dateReservation, LocalTime heureReservation, int nombreBillets, StatutReservation statut) {
        this(idUtilisateur, idAttraction, dateReservation, heureReservation, nombreBillets, statut);
        this.id = id;
    }

    // === Getters & Setters ===

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


    @Override
    public String toString() {
        return "Réservation #" + id + " : Attraction " + idAttraction + " à " + heureReservation + "h le " + dateReservation;
    }
}