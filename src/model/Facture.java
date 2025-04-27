package model;

import java.time.LocalDate;

public class Facture {

    private int id;
    private int idReservation;
    private double montantTotal;
    private LocalDate dateFacture;
    private boolean reductionAppliquee;

    // Constructeurs

    public Facture(int id, int idReservation, double montantTotal, LocalDate dateFacture, boolean reductionAppliquee) {
        this.id = id;
        this.idReservation = idReservation;
        this.montantTotal = montantTotal;
        this.dateFacture = dateFacture;
        this.reductionAppliquee = reductionAppliquee;
    }

    public Facture(int idReservation, double montantTotal, LocalDate dateFacture, boolean reductionAppliquee) {
        this.idReservation = idReservation;
        this.montantTotal = montantTotal;
        this.dateFacture = dateFacture;
        this.reductionAppliquee = reductionAppliquee;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdReservation() {
        return idReservation;
    }


    public double getMontantTotal() {
        return montantTotal;
    }



    public LocalDate getDateFacture() {
        return dateFacture;
    }


    public boolean isReductionAppliquee() {
        return reductionAppliquee;
    }


    @Override
    public String toString() {
        return "Facture #" + id + " - " + montantTotal + "€ le " + dateFacture +
                (reductionAppliquee ? " (réduction appliquée)" : "");
    }
}