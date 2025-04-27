package controller;

import dao.FactureDAO;
import dao.ReservationDAO;
import dao.AttractionDAO;
import dao.UtilisateurDAO;
import model.Attraction;
import model.Facture;
import model.Reservation;
import model.Utilisateur;
import model.Reduction;
import dao.ReductionDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservationController {

    private final ReservationDAO reservationDAO;
    private final FactureDAO factureDAO;
    private final AttractionDAO attractionDAO;
    private final UtilisateurDAO utilisateurDAO;
    private final ReductionDAO reductionDAO;

    public ReservationController() {
        this.reservationDAO = new ReservationDAO();
        this.factureDAO = new FactureDAO();
        this.attractionDAO = new AttractionDAO();
        this.utilisateurDAO = new UtilisateurDAO();
        this.reductionDAO = new ReductionDAO();
    }

    /**
     * Crée une nouvelle réservation avec date et heure.
     */
    public boolean reserverAttraction(int idUtilisateur, int idAttraction, LocalDate date, LocalTime heure, int nbBillets) {
        if (nbBillets <= 0 || date.isBefore(LocalDate.now())) {
            return false;
        }

        Reservation reservation = new Reservation(
                idUtilisateur,
                idAttraction,
                date,
                heure,
                nbBillets,
                Reservation.StatutReservation.CONFIRMEE
        );

        int reservationId = reservationDAO.insertReservation(reservation);

        if (reservationId > 0) {
            reservation.setId(reservationId);

            Attraction attraction = attractionDAO.getAttractionById(idAttraction);
            Utilisateur utilisateur = utilisateurDAO.getUtilisateurById(idUtilisateur);

            double prixUnitaire = attraction.getPrix();
            double montantTotal = prixUnitaire * nbBillets;
            boolean reductionAppliquee = false;

            // Nouvelle vérification ici
            if (utilisateur != null && utilisateur.getType() != Utilisateur.TypeUtilisateur.INVITE) {
                int age = utilisateur.getAge();
                List<Reduction> reductions = reductionDAO.getAllReductions();

                for (Reduction r : reductions) {
                    boolean applicable =
                            (r.getCritere() == Reduction.CritereReduction.ENFANT && age < 12) ||
                                    (r.getCritere() == Reduction.CritereReduction.SENIOR && age > 60) ||
                                    (r.getCritere() == Reduction.CritereReduction.FIDELITE && utilisateur.getType() == Utilisateur.TypeUtilisateur.MEMBRE);

                    if (applicable) {
                        montantTotal *= (1 - (r.getPourcentage() / 100.0));
                        reductionAppliquee = true;
                        break;
                    }
                }
            }

            Facture facture = new Facture(
                    reservationId,
                    montantTotal,
                    LocalDate.now(),
                    reductionAppliquee
            );

            factureDAO.insertFacture(facture);

            return true;
        }

        return false;
    }

    /**
     * Historique des réservations du client
     */
    public List<Reservation> getHistoriqueUtilisateur(int idUtilisateur) {
        return reservationDAO.getReservationsByUtilisateur(idUtilisateur);
    }

    /**
     * Liste de toutes les réservations (admin)
     */
    public List<Reservation> getToutesReservations() {
        return reservationDAO.getAllReservations();
    }

    public boolean supprimerReservation(int idReservation) {
        return reservationDAO.deleteReservation(idReservation);
    }
}