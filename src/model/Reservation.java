package model;

import java.time.LocalDate;

public class Reservation {

    private int id;
    private int idUtilisateur;
    private int idAttraction;
    private LocalDate dateReservation;
    private int nombreBillets;
    private StatutReservation statut;

    // Enumération des statuts possibles
    public enum StatutReservation {
        CONFIRMEE, ANNULEE, EN_ATTENTE
    }

    // Constructeurs
    public Reservation() {}

    public Reservation(int id, int idUtilisateur, int idAttraction, LocalDate dateReservation, int nombreBillets, StatutReservation statut) {
        this.id = id;
        this.idUtilisateur = idUtilisateur;
        this.idAttraction = idAttraction;
        this.dateReservation = dateReservation;
        this.nombreBillets = nombreBillets;
        this.statut = statut;
    }

    public Reservation(int idUtilisateur, int idAttraction, LocalDate dateReservation, int nombreBillets, StatutReservation statut) {
        this.idUtilisateur = idUtilisateur;
        this.idAttraction = idAttraction;
        this.dateReservation = dateReservation;
        this.nombreBillets = nombreBillets;
        this.statut = statut;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdAttraction() {
        return idAttraction;
    }

    public void setIdAttraction(int idAttraction) {
        this.idAttraction = idAttraction;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    public int getNombreBillets() {
        return nombreBillets;
    }

    public void setNombreBillets(int nombreBillets) {
        this.nombreBillets = nombreBillets;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Réservation de " + nombreBillets + " billet(s) le " + dateReservation + " - Statut : " + statut;
    }
}