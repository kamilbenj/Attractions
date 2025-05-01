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

    public ClientController() {
        this.reservationDAO = new ReservationDAO();
        this.factureDAO = new FactureDAO();
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