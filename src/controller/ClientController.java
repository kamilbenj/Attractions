package controller;

import dao.FactureDAO;
import dao.ReservationDAO;
import dao.UtilisateurDAO;
import dao.UtilisateurReductionDAO;
import model.Facture;
import model.Reservation;
import model.UtilisateurReduction;

import java.util.List;

public class ClientController {

    private final ReservationDAO reservationDAO;
    private final FactureDAO factureDAO;
    private final UtilisateurReductionDAO utilisateurReductionDAO;
    private final UtilisateurDAO utilisateurDAO;

    public ClientController() {
        this.reservationDAO = new ReservationDAO();
        this.factureDAO = new FactureDAO();
        this.utilisateurReductionDAO = new UtilisateurReductionDAO();
        this.utilisateurDAO = new UtilisateurDAO();
    }

    /**
     * Retourne l’historique des réservations du client.
     */
    public List<Reservation> getReservationsClient(int idClient) {
        return reservationDAO.getReservationsByUtilisateur(idClient);
    }

    /**
     * Retourne toutes les factures liées aux réservations du client.
     */
    public List<Facture> getFacturesClient(int idClient) {
        List<Reservation> reservations = reservationDAO.getReservationsByUtilisateur(idClient);
        List<Facture> facturesClient = new java.util.ArrayList<>();

        for (Reservation r : reservations) {
            facturesClient.addAll(factureDAO.getFacturesByReservation(r.getId()));
        }

        return facturesClient;
    }

    /**
     * Retourne les réductions assignées à un client.
     */
    public List<UtilisateurReduction> getReductionsClient(int idClient) {
        return utilisateurReductionDAO.getReductionsByUtilisateur(idClient);
    }

    /**
     * Permet à un client de supprimer son compte.
     */
    public boolean supprimerCompte(int idClient) {
        return utilisateurDAO.deleteUtilisateur(idClient);
    }
}