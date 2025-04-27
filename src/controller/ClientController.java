package controller;

import dao.FactureDAO;
import dao.ReservationDAO;
import dao.UtilisateurDAO;
import model.Facture;
import model.Reservation;

import java.util.List;

public class ClientController {

    private final ReservationDAO reservationDAO;
    private final FactureDAO factureDAO;
    private final UtilisateurDAO utilisateurDAO;

    public ClientController() {
        this.reservationDAO = new ReservationDAO();
        this.factureDAO = new FactureDAO();
        this.utilisateurDAO = new UtilisateurDAO();
    }

    // Retourne l’historique des réservations du client.

    public List<Reservation> getReservationsClient(int idClient) {
        return reservationDAO.getReservationsByUtilisateur(idClient);
    }

    //Retourne toutes les factures liées aux réservations du client.
    public List<Facture> getFacturesClient(int idClient) {
        List<Reservation> reservations = reservationDAO.getReservationsByUtilisateur(idClient);
        List<Facture> facturesClient = new java.util.ArrayList<>();

        for (Reservation r : reservations) {
            facturesClient.addAll(factureDAO.getFacturesByReservation(r.getId()));
        }

        return facturesClient;
    }
}